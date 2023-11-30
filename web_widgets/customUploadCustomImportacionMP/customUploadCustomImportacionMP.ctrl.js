function UploadCustomImportacionPAA($scope, $http, blockUI) {
    $scope.fileName = "";

    var input = document.querySelector('input[type=file]');

    $scope.cleanInput = function() {
        var inputToClear = document.querySelector('input[type=file]');
        inputToClear.type = "text";
        inputToClear.type = "file";
    };

    input.onchange = function() {
        var file = input.files[0],
            reader = new FileReader();


        if (reader.readAsText) {
            //if (reader.readAsBinaryString) {
            reader.onload = function(e) {
                processData(e.target.result)
                    //ProcessExcel(e.target.result);
            };
            reader.readAsText(file, "UTF-8")
        } else {
            //For IE Browser.
            reader.onload = function(e) {
                var data = "";
                var bytes = new Uint8Array(e.target.result);
                for (var i = 0; i < bytes.byteLength; i++) {
                    data += String.fromCharCode(bytes[i]);
                }
                ProcessExcel(data);
            };
            reader.readAsArrayBuffer(file);
        }
    };

    function processData(allText) {
        debugger
        var allTextLines = allText.split(/\r\n|\n/);
        var headers = allTextLines[0].split(',');
        var lines = [];
        if (allTextLines.length > 1) {
            for (var i = 1; i < allTextLines.length; i++) {
                var data = csvToArray(allTextLines[i]);
                if (data.length == headers.length) {
                    var tarr = [];
                    for (var j = 0; j < headers.length; j++) {
                        tarr[headers[j]] = data[j];
                    }
                    lines.push(needValues(tarr));
                }
            }
            auditoria(lines)
            $scope.$apply();
        }

    }

    function csvToArray(text) {
        var re_valid = /^\s*(?:'[^'\\]*(?:\\[\S\s][^'\\]*)*'|"[^"\\]*(?:\\[\S\s][^"\\]*)*"|[^,'"\s\\]*(?:\s+[^,'"\s\\]+)*)\s*(?:,\s*(?:'[^'\\]*(?:\\[\S\s][^'\\]*)*'|"[^"\\]*(?:\\[\S\s][^"\\]*)*"|[^,'"\s\\]*(?:\s+[^,'"\s\\]+)*)\s*)*$/;
        var re_value = /(?!\s*$)\s*(?:'([^'\\]*(?:\\[\S\s][^'\\]*)*)'|"([^"\\]*(?:\\[\S\s][^"\\]*)*)"|([^,'"\s\\]*(?:\s+[^,'"\s\\]+)*))\s*(?:,|$)/g;

        if (!re_valid.test(text)) return null;
        var a = []; // Initialize array to receive values.
        text.replace(re_value, // "Walk" the string using replace with callback.
            function(m0, m1, m2, m3) {
                // Remove backslash from \' in single quoted values.
                if (m1 !== undefined) a.push(m1.replace(/\\'/g, "'"));
                // Remove backslash from \" in double quoted values.
                else if (m2 !== undefined) a.push(m2.replace(/\\"/g, '"'));
                else if (m3 !== undefined) a.push(m3);
                return ''; // Return empty string.
            });
        // Handle special case of empty last value.
        if (/,\s*$/.test(text)) a.push('');

        return a;
    }

    function uploadFile(fileObject) {
        let url = "/bonita/API/extension/AnahuacRest?url=getInformacionResultado&p=0&c=100";
        $http.post(url, fileObject).success(function(succaess) {
            $scope.properties.lstAlumnosResultados = success.data;
            $scope.properties.totalRows = success.data.length;
        }).error(function() {
            $scope.cleanInput();
            swal("Error", "El archivo proporcionado no es válido, favor de subir un archivo de resultados válido y en formato excel (.xlsx).", "error");
        });
    }

    function ProcessExcel(data) {
        //Read the Excel File data.
        var workbook = XLSX.read(data, {
            type: 'binary',
            encoding: "UTF-8"
        });

        //Fetch the name of First Sheet.
        var firstSheet = workbook.SheetNames[0];
        console.log(firstSheet)
            //Read all rows from First Sheet into an JSON array.
        var excelRows = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[firstSheet]);
        console.log(excelRows)
        if (excelRows.length >= 1) {
            var datos = [];
            //Add the data rows from Excel file.
            for (var i = 0; i < excelRows.length; i++) {
                datos.push(needValues(excelRows[i]))
            }
            auditoria(datos)
            $scope.$apply();
        } else {
            swal("Error", "El archivo se encuentra en blanco", "error");
        }


    }

    $scope.errores = [];
    $scope.correctos = [];
    $scope.final = [];
    $scope.lstBanner = {};

    async function auditoria(row) {

        var count = 0;
        $scope.errores = [];
        $scope.correctos = [];
        $scope.final = [];
        $scope.lstBanner = { 'IDBANNER': "", "FECHA": "", "IDSESION": "" };

        blockUI.start();
        if (!isNullOrUndefined(row)) {
            row.forEach(datos => {
                count++;
                var info = datos;
                //info.fechaExamen = info['Fecha de examen']

                let paso = validacion(info);
                if (paso) {
                    info.tipoExamen = "MetaProfile";
                    info.INVP = "";
                    $scope.correctos = [...$scope.correctos, info];
                }
                if (count === row.length) {
                    $scope.correctos = findDuplicados($scope.correctos, "IDBANNER");
                    cargarlstBanner($scope.correctos);
                    if ($scope.correctos.length > 0) {
                        doRequest("POST", $scope.properties.urlPost, $scope.lstBanner).then(function() {
                            if (count == row.length) {
                                $scope.properties.lstBanner = $scope.lstBanner;
                                $scope.properties.lstErrores = angular.copy($scope.errores)
                                $scope.properties.lstAlumnosResultados = angular.copy($scope.final)
                                $scope.properties.tabla = "carga";
                                //swal('¡Se han terminado la auditoria de los datos!',"","success")
                            }
                        });
                    } else {
                        $scope.properties.lstErrores = angular.copy($scope.errores)
                        $scope.properties.lstAlumnosResultados = [];
                        $scope.properties.tabla = "carga";
                    }
                }
            });
        }
        blockUI.stop();

    }

    function needValues(data) {
        let info = {};
        $scope.properties.revisar.forEach(valor => {
            info = Object.assign({
                [valor]: (data[valor] || '') }, info)
        })
        info.fechaExamen = (data['Fechadeexamen'] || '');
        info.espanol = (data['Espanol'] || '');
        info.matematicas = (data['Matematicas'] || '');
        info.habilidadesBlandas = (data['Habilidades Blandas'] || '');
        info.Nombre = (data['Nombre'] || '');
        info.PAAN = (data['Espanol'] || '');
        info.PAAV = (data['Matematicas'] || '');
        info.PARA = (data['Habilidades Blandas'] || '');
        info.Total = "0";
        info.metaprofiletotal = "0";
        return info;
    }

    function validacion(data) {
        var error = "";
        let valores1 = ["Espanol", "Matematicas", "Habilidades blandas"];
        let valores2 = ["PAAN", "PAAV", "PARA"];
        if (data !== null && data !== undefined) {
            //data['PAAN'] = revisarEscalaEAC(data['PAAN']);
            //data['PAAV'] = revisarEscalaEAC(data['PAAV']);
            //data['PARA'] =revisarEscalaEAC(data['PARA']);
            let columnas = $scope.properties.revisar;
            for (let i = 0; i < $scope.properties.revisar.length; i++) {
                if (isNullOrUndefined(data[columnas[i]]) && columnas[i] != "IDBANNER" && columnas[i] != "Fechadeexamen") {
                    error += (error.length > 0 ? "," : "") + "falta el dato " + columnas[i]
                } else if (columnas[i] == "IDBANNER") {
                    if (isNullOrUndefined(data[columnas[i]])) {
                        error += (error.length > 0 ? "," : "") + "falta el dato id banner "
                    } else if (data[columnas[i]].length != 8) {
                        error += (error.length > 0 ? "," : "") + "id banner tiene que ser de 8 digitos"
                    }
                } else if (columnas[i] == "Fechadeexamen") {
                    if (isNullOrUndefined(data[columnas[i]])) {
                        error += (error.length > 0 ? "," : "") + "falta el dato fecha de examen "
                    } else if (!moment(data[columnas[i]], 'DD/MM/YYYY').isValid()) {
                        error += (error.length > 0 ? "," : "") + "la fecha no es valida tiene que ser DD/MM/YYYY"
                    }
                } else if (valores1.includes(columnas[i]) && !isRangoValue(data[columnas[i]], 200, 800)) {
                    error += (error.length > 0 ? "," : "") + columnas[i] + `\xa0(${data[columnas[i]]})\xa0tiene que estar en el rango de 200-800`;
                }else if (valores2.includes(columnas[i]) && !isRangoValue(data[columnas[i]], 200, 800)) {
                    error += (error.length > 0 ? "," : "") + columnas[i] + `\xa0(${data[columnas[i]]})\xa0tiene que estar en el rango de 200-800`;
                }
            }

            if (error.length > 0) {
                $scope.errores = [...$scope.errores, { idBanner: data.IDBANNER, nombre: data.Nombre, Error: error }]
                return false;
            }
            
            data['metaprofiletotal'] = "" + (parseInt(data.espanol.toString()) + parseInt(data.matematicas.toString()) + parseInt(data.habilidadesBlandas.toString()));
            //data['Total'] = "" + (parseInt(data.espanol.toString()) + parseInt(data.matematicas.toString()) + parseInt(data.habilidadesBlandas.toString()));
            data['Total'] = "" + (parseInt(data.PAAN.toString()) + parseInt(data.PAAV.toString()) + parseInt(data.PARA.toString()));
            return true;
        }
        $scope.errores = [...$scope.errores, { idBanner: data.IDBANNER, nombre: data.nombre, Error: "datos en blanco" }]
        return false;
    }

    function isNullOrUndefined(dato) {
        if (dato === undefined || dato === null || dato.length <= 0) {
            return true;
        }
        return false
    }

    function isRangoValue(value, min, max) {
        if (value >= min && value <= max) {
            return true;
        }
        return false;
    }


    function haveZero(value) {
        if (value.includes("0") && value.length == 2) {
            return true;
        }
        return false;
    }




    function doRequest(method, url, datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function(data, status) {
                revisarDatos(data, $scope.correctos);
            })
            .error(function(data, status) {})
    }


    function revisarDatos(data, datos) {
        data.data.forEach((info) => {
            let indice = findData(info.idBanner.replaceAll("'", ""))
            if (!info.Existe) {
                $scope.errores = [...$scope.errores, { idBanner: datos[indice].IDBANNER, nombre: datos[indice].Nombre, Error: "Id banner incorrecto o no se encuentra" }]
            } else if (info.mismaFecha) {
                $scope.errores = [...$scope.errores, { idBanner: datos[indice].IDBANNER, nombre: datos[indice].Nombre, Error: `El aspirante ya cuenta con una puntuacion anterior en la fecha ${datos[indice]["fechaExamen"]}` }]
            }
            // else if(!info.EstaEnCarga){
            // $scope.errores = [ ...$scope.errores,{idBanner:datos[indice].IDBANNER,nombre:datos[indice].Nombre,Error:"El aspirante no se encuentra en carga y consulta de resultados"}]
            // }
            else if (info.AA) {
                $scope.errores = [...$scope.errores, { idBanner: datos[indice].IDBANNER, nombre: datos[indice].Nombre, Error: "Este aspirante tendra que ser cargado manual ya que cuenta con una puntuacion registrada" }]
            } else if (!info.puede) {
                $scope.errores = [...$scope.errores, { idBanner: datos[indice].IDBANNER, nombre: datos[indice].Nombre, Error: "El aspirante ya cuenta con una puntuacion anterior" }]
            } else if (!info.sc) {
                $scope.errores = [...$scope.errores, { idBanner: datos[indice].IDBANNER, nombre: datos[indice].Nombre, Error: "El aspirante no se encuentra en la sesion subida" }]
            } else {
                //hacer la conversion segun la tabla y guardar los valores originales para mostrar
                datos[indice].caseId = info.caseId;
                $scope.final = [...$scope.final, datos[indice]]
            }
        })

    }


    function findData(valor) {
        let index = $scope.correctos.findIndex(function(item, i) {
            return item.IDBANNER === valor
        });
        return index;
    }

    function findDuplicados(yourArray, prop) {
        //encuantra los duplicados en vase a un campo del json-array
        let newArray = [];
        for (let i = 0; i < yourArray.length; i++) {
            for (let j = 0; j < yourArray.length; j++) {
                if (yourArray[i][prop] == yourArray[j][
                        [prop]
                    ] && i != j) {
                    //se obtiene los duplicados
                    newArray.push(yourArray[i][prop])
                }
            }
        }
        // limpia los duplicados [1,1] = [1]
        const Duplicates = [...new Set(newArray)]
        let count = yourArray.length;
        for (let i = 0; i < Duplicates.length; i++) {

            while (count--) {
                if (yourArray[count][prop] == Duplicates[i]) {
                    $scope.errores = [...$scope.errores, { idBanner: yourArray[count]['IDBANNER'], nombre: yourArray[count]['Nombre'], Error: "Id banner se encuentra duplicado en el excel" }]
                    yourArray.splice(count, 1);
                }
            }
        }
        console.log(yourArray);
        return yourArray;
    }

    function cargarlstBanner(arreglo) {
        $scope.lstBanner = { 'IDBANNER': "", "FECHA": "", "IDSESION": "" };
        arreglo.forEach(info => {
            $scope.lstBanner.IDBANNER += `${$scope.lstBanner.IDBANNER.length>0?",":""}'${info['IDBANNER']}'`;
            $scope.lstBanner.FECHA += `${$scope.lstBanner.FECHA.length>0?",":""}'${info['Fechadeexamen']}'`;
            $scope.lstBanner.IDSESION += `${$scope.lstBanner.IDSESION.length>0?",":""}'${info['IdSesion']}'`;
        });
    }


}