function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {

    'use strict';

    var vm = this;

    $scope.date = new Date();
    $scope.fechaActual = ($filter('date')(Date.parse($scope.date), "dd/MMM/yyyy")).toString();

    //GENERAR PDF - AdataR
    $scope.generatePDF = function() {
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        var yValor = 0;
        var yvalor = 0;

        var fontText = 10;
        var fontTitle = 14;
        var fontSubTitle = 13;

        var margenPrimeraFila = 15;
        var margenSegundaFila = 135;
        var margenFilaIntermedia = 80;

        var respuestasPrimeraFila = 15;
        var respuestasSegundaFila = 135;
        var respuestasFilaIntermedia = 80;

        var countFinSeccionTutor;
        $scope.lstArchivedCase = [];
        $scope.caseId = 0;
        $scope.taskId = 0;
        $scope.lstArchivedCase = {};

        var GET_parameters = {};

        if (location.search) {
            var splitts = location.search.substring(1).split('&');
            for (var i = 0; i < splitts.length; i++) {
                var key_value_pair = splitts[i].split('=');
                if (!key_value_pair[0]) continue;
                GET_parameters[key_value_pair[0]] = key_value_pair[1] || true;
            }
        }

        $scope.caseId = GET_parameters.caseId;

        function getCurrentTask() {
            let url = "../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=" + $scope.caseId + "&fstate=ready";

            var req = {
                method: "GET",
                url: url
            };

            return $http(req)
                .success(function(data, status) {
                    // let message = "SE CAMBIO EL ID DE LA TAREA, EL VIEJO ES " 
                    // + $scope.properties.taskId 
                    // + " EL NUEVO ES "
                    // + data[0].id;
                    // swal("OK", message, "success");
                    if (data.length > 0) {

                        $scope.properties.taskId = data[0].id;
                    } else {

                    }
                })
                .error(function(data, status) {
                    $scope.loadCaseId();
                })
                .finally(function() {
                    // vm.busy = false;
                });
        }


        $scope.loadCaseId = function() {
            doRequest("GET", "/bonita/API/system/session/unusedid", null, function(data, status) {
                    doRequest("GET", "../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico=" + data.user_name + "&p=0&c=500", null, function(data, status) {
                        if (data.length > 0) {
                            $scope.caseId = data[0].caseId;

                        } else {
                            $scope.caseId = getUrlParam("caseId");
                        }
                        //  ../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid={{caseList[0].caseId}}&fstate=ready
                        doRequest("GET", "../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=" + $scope.caseId + "&fstate=ready", null, function(data, status) {
                            $scope.taskId = data[0].id;
                            $scope.properties.taskId = $scope.taskId;
                            doRequest("GET", "../API/bpm/userTask/" + $scope.taskId + "/context", null, function(context, status) {
                                $scope.properties.context = context;

                            }, function(data, status) {
                                doRequest("GET", "../API/extension/RegistroRest?url=archivedCase&caseid=" + $scope.caseId, {},
                                    function(data, status) { //SUCCESS
                                        $scope.lstArchivedCase = data[0];
                                        if (data.length > 0) {
                                            doRequest("GET", "../API/bpm/archivedCase/" + $scope.lstArchivedCase.id + "/context", {},
                                                function(data, status) { //SUCCESS
                                                    $scope.properties.context = data;
                                                },
                                                function(data, status) { //ERROR

                                                })
                                        }
                                    },
                                    function(data, status) { //ERROR

                                    })


                            })
                        }, function(data, status) {

                        })
                    }, function(data, status) {

                    })
                }, function(data, status) {

                })
                //../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico={{session.user_name}}&p=0&c=500
        }


        console.log("W " + width)
        console.log("H " + height)
        console.log($scope.properties.data)

        doc.addImage("widgets/customBtnADPDF/assets/img/LogoRUA.png", "PNG", ((width / 2) + 35), ((height / 2) - 128), 60, 20);

        doc.setFontSize(fontText);
        doc.text(margenSegundaFila, (height / 2) - 140, 'Fecha:');
        doc.text($scope.fechaActual, 155, (height / 2) - 140);
        
        doc.text(margenSegundaFila, (height / 2) - 135, 'Usuario:');
        doc.text($scope.properties.data.user_name, 155, (height / 2) - 135);
        




        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text('SOLICITUD DE ADMISIÓN', margenPrimeraFila, (height / 2) - 116);

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 103, 190, 85, 'F');

        //  ----------------------------------------- PRIMERA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 93, 'Información de la solicitud');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 83, 'Campus donde cursará sus estudios:');
        doc.text(margenPrimeraFila, (height / 2) - 71, 'Licenciatura:');
        doc.text(margenPrimeraFila, (height / 2) - 59, 'Período de ingreso:');
        doc.text(margenPrimeraFila, (height / 2) - 47, 'Realizo proceso de admisión en otra');
        doc.text(margenPrimeraFila, (height / 2) - 42, 'universidad de la red Anáhuac:');
        doc.text(margenPrimeraFila, (height / 2) - 30, 'Lugar donde realizarás tu examen:');

        doc.setDrawColor(115, 66, 34)
            //doc.rect(margenSegundaFila,((height / 2)-84),35,45)
        doc.rect(157.5, ((height / 2) - 84), 35, 45)

        if($scope.properties.rotacion){
            doc.addImage($scope.properties.urlFoto, "JPG", 160, ((height / 2) - 112), 40, 30,'','NONE',270);
        }else{
            doc.addImage($scope.properties.urlFoto, "JPG", 160, ((height / 2) - 82), 30, 40);
        }
        


        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catCampusEstudio.descripcion, respuestasPrimeraFila, (height / 2) - 78);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catGestionEscolar.nombre, respuestasPrimeraFila, (height / 2) - 66);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPeriodo.descripcion, respuestasPrimeraFila, (height / 2) - 54);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion, respuestasPrimeraFila, (height / 2) - 37);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catCampus.descripcion, respuestasPrimeraFila, (height / 2) - 25);

        //  ----------------------------------------- SEGUNDA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 12, 'Información personal');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 2, 'Nombre (s):');
        doc.text(margenFilaIntermedia, (height / 2) - 2, 'Apellido paterno:');
        doc.text(margenSegundaFila, (height / 2) - 2, 'Apellido materno:');

        doc.text(margenPrimeraFila, (height / 2) + 10, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) + 10, 'Fecha de nacimiento:');
        doc.text(margenSegundaFila, (height / 2) + 10, 'Sexo:');

        doc.text(margenPrimeraFila, (height / 2) + 22, 'Nacionalidad:');
        doc.text(margenFilaIntermedia, (height / 2) + 22, 'Religión:');
        doc.text(margenSegundaFila, (height / 2) + 22, 'CURP:');

        doc.text(margenPrimeraFila, (height / 2) + 34, 'Número de pasaporte:');
        doc.text(margenFilaIntermedia, (height / 2) + 34, 'Estado civil:');
        doc.text(margenSegundaFila, (height / 2) + 34, 'Teléfono celular:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.primerNombre + " " + $scope.properties.PDFobjSolicitudDeAdmision.segundoNombre, respuestasPrimeraFila, (height / 2) + 3);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.apellidoPaterno, respuestasFilaIntermedia, (height / 2) + 3);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.apellidoMaterno, respuestasSegundaFila, (height / 2) + 3);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.correoElectronico, respuestasPrimeraFila, (height / 2) + 15);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.fechaNacimiento.slice(0,10), respuestasFilaIntermedia, (height / 2) + 15);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catSexo.descripcion, respuestasSegundaFila, (height / 2) + 15);
        debugger
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catNacionalidad.descripcion, respuestasPrimeraFila, (height / 2) + 27);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catReligion.descripcion, respuestasFilaIntermedia, (height / 2) + 27);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.curp == "" || $scope.properties.PDFobjSolicitudDeAdmision.curp == null ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.curp), respuestasSegundaFila, (height / 2) + 27);

        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.curp == "" || $scope.properties.PDFobjSolicitudDeAdmision.curp == null ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.curp), respuestasPrimeraFila, (height / 2) + 39);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catEstadoCivil.descripcion, respuestasFilaIntermedia, (height / 2) + 39);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.telefonoCelular, respuestasSegundaFila, (height / 2) + 39);

        //  ----------------------------------------- TERCERA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) + 48, 190, 75, 'F');

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 58, 'Domicilio permanente');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 70, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) + 70, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) + 70, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 84.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 82, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 87, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 84.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 99, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 99, 'Entre calles:');
        doc.text(margenSegundaFila, (height / 2) + 99, 'Núm. Exterior');

        doc.text(margenPrimeraFila, (height / 2) + 111, 'Núm. Interior:');
        doc.text(margenFilaIntermedia, (height / 2) + 111, 'Teléfono:');
        doc.text(margenSegundaFila, (height / 2) + 111, 'Otro teléfono de contacto:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPais.descripcion, respuestasPrimeraFila, (height / 2) + 75);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.codigoPostal, respuestasFilaIntermedia, (height / 2) + 75);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision?.catEstado?.descripcion == null || $scope.properties.PDFobjSolicitudDeAdmision?.catEstado?.descripcion == "" ? $scope.properties.PDFobjSolicitudDeAdmision.estadoExtranjero : $scope.properties.PDFobjSolicitudDeAdmision?.catEstado?.descripcion), respuestasSegundaFila, (height / 2) + 75);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.ciudad, respuestasPrimeraFila, (height / 2) + 88.5);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 92);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.colonia, respuestasSegundaFila, (height / 2) + 88.5);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.calle, respuestasPrimeraFila, (height / 2) + 104);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.calle2 == null || $scope.properties.PDFobjSolicitudDeAdmision.calle2 == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.calle2), respuestasFilaIntermedia, (height / 2) + 104);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.numExterior == null || $scope.properties.PDFobjSolicitudDeAdmision.numExterior == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.numExterior), respuestasSegundaFila, (height / 2) + 104);

        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.numInterior == null || $scope.properties.PDFobjSolicitudDeAdmision.numInterior == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.numInterior), respuestasPrimeraFila, (height / 2) + 116);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.telefono, respuestasFilaIntermedia, (height / 2) + 116);
        doc.text(( $scope.properties.PDFobjSolicitudDeAdmision.otroTelefonoContacto == null || $scope.properties.PDFobjSolicitudDeAdmision.otroTelefonoContacto == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.otroTelefonoContacto), respuestasSegundaFila, (height / 2) + 116);

        doc.addPage();
        //  ----------------------------------- NUEVA HOJA Y CUARTA SECCIÓN  ----------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 116, 'Información del bachillerato');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 106, 'Nombre bachillerato:');
        doc.text(margenFilaIntermedia, (height / 2) - 106, 'País de tu bachillerato:');
        doc.text(margenSegundaFila, (height / 2) - 106, 'Estado de tu bachillerato:');

        doc.text(margenPrimeraFila, (height / 2) - 94, 'Ciudad de tu bachillerato');
        doc.text(margenFilaIntermedia, (height / 2) - 94, 'Promedio general:');
        doc.text(margenSegundaFila, (height / 2) - 94, 'Resultado PAA:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.descripcion, respuestasPrimeraFila, (height / 2) - 101);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.pais == null ? $scope.properties.PDFobjSolicitudDeAdmision.paisBachillerato : $scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.pais), respuestasFilaIntermedia, (height / 2) - 101);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.estado == null ? $scope.properties.PDFobjSolicitudDeAdmision.estadoBachillerato : $scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.estado), respuestasSegundaFila, (height / 2) - 101);

        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.ciudad == null ? $scope.properties.PDFobjSolicitudDeAdmision.ciudad : $scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.ciudad), respuestasPrimeraFila, (height / 2) - 89);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.promedioGeneral, respuestasFilaIntermedia, (height / 2) - 89);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.resultadoPAA.toString() <= 0 ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.resultadoPAA.toString()), respuestasSegundaFila, (height / 2) - 89);

        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 84, 190, 130, 'F');

        //$scope.properties.PDFobjTutor.forEach(function(PDFitem, i, objeto) {


        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 74, 'Información del tutor');

        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 64, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) - 64, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) - 64, 'Apellido (s):');



        doc.text(margenPrimeraFila, (height / 2) - 52, 'Parentesco:');
        doc.text(margenFilaIntermedia, (height / 2) - 52, 'Correo eletrónico:');
        doc.text(margenSegundaFila, (height / 2) - 52, 'Escolaridad del tutor:');

        doc.text(margenPrimeraFila, (height / 2) - 40, 'Ocupación del tutor:');
        doc.text(margenFilaIntermedia, (height / 2) - 40, 'Empresa:');
        doc.text(margenSegundaFila, (height / 2) - 40, 'Universidad Anáhuac:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjTutor[0].catTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 59);
        doc.text($scope.properties.PDFobjTutor[0].nombre, respuestasFilaIntermedia, (height / 2) - 59);
        doc.text($scope.properties.PDFobjTutor[0].apellidos, respuestasSegundaFila, (height / 2) - 59);

        doc.text($scope.properties.PDFobjTutor[0].catParentezco.descripcion, respuestasPrimeraFila, (height / 2) - 47);
        doc.text($scope.properties.PDFobjTutor[0].correoElectronico, respuestasFilaIntermedia, (height / 2) - 47);
        doc.text($scope.properties.PDFobjTutor[0].catEscolaridad.descripcion, respuestasSegundaFila, (height / 2) - 47);

        doc.text(($scope.properties.PDFobjTutor[0].puesto == null || $scope.properties.PDFobjTutor[0].puesto == "" ? "No trabaja" : $scope.properties.PDFobjTutor[0].puesto), respuestasPrimeraFila, (height / 2) - 35);
        doc.text(($scope.properties.PDFobjTutor[0].empresaTrabaja == null || $scope.properties.PDFobjTutor[0].empresaTrabaja == "" ? "No trabaja" : $scope.properties.PDFobjTutor[0].empresaTrabaja), respuestasFilaIntermedia, (height / 2) - 35);
        doc.text(($scope.properties.PDFobjTutor[0].catCampusEgreso == null ? "No" : $scope.properties.PDFobjTutor[0].catCampusEgreso.descripcion), respuestasSegundaFila, (height / 2) - 35);

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 20, 'Domicilio permanente del tutor');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 10, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) - 10, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) - 10, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 3.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 2, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 7, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 3.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 19, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 19, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) + 19, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) + 31, 'Teléfono:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjTutor[0].catPais.descripcion, respuestasPrimeraFila, (height / 2) - 5);
        doc.text($scope.properties.PDFobjTutor[0].codigoPostal, respuestasFilaIntermedia, (height / 2) - 5);
        doc.text(($scope.properties.PDFobjTutor[0]?.catEstado?.descripcion == null || $scope.properties.PDFobjTutor[0]?.catEstado?.descripcion == "" ? $scope.properties.PDFobjTutor[0]?.estadoExtranjero : $scope.properties.PDFobjTutor[0]?.catEstado?.descripcion) , respuestasSegundaFila, (height / 2) - 5);

        doc.text($scope.properties.PDFobjTutor[0].ciudad, respuestasPrimeraFila, (height / 2) + 7.5);
        doc.text($scope.properties.PDFobjTutor[0].delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 12);
        doc.text($scope.properties.PDFobjTutor[0].colonia, respuestasSegundaFila, (height / 2) + 7.5);

        doc.text($scope.properties.PDFobjTutor[0].calle, respuestasPrimeraFila, (height / 2) + 24);
        doc.text(( $scope.properties.PDFobjTutor[0].numeroExterior == null || $scope.properties.PDFobjTutor[0].numeroExterior == "" ? "N/A" : $scope.properties.PDFobjTutor[0].numeroExterior), respuestasFilaIntermedia, (height / 2) + 24);
        doc.text(( $scope.properties.PDFobjTutor[0].numeroInterior == null || $scope.properties.PDFobjTutor[0].numeroInterior == "" ? "N/A" : $scope.properties.PDFobjTutor[0].numeroInterior), respuestasSegundaFila, (height / 2) + 24);
        doc.text($scope.properties.PDFobjTutor[0].telefono, respuestasPrimeraFila, countFinSeccionTutor = ((height / 2) + 36));

        console.log(countFinSeccionTutor)

        //})

        //  ----------------------------------------- SEXTA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 55, 'Información del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 65, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) + 65, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) + 65, 'Apellido (s):');

        doc.text(margenPrimeraFila, (height / 2) + 77, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) + 77, 'Escolaridad del padre:');
        doc.text(margenSegundaFila, (height / 2) + 77, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) + 89, 'Ocupación del padre:');
        doc.text(margenFilaIntermedia, (height / 2) + 89, 'Empresa:');

        //Respuestas
        doc.setFont(undefined, 'normal');

        if ($scope.properties.PDFobjPadreCatTitulo != null) {
            doc.text($scope.properties.PDFobjPadreCatTitulo.descripcion, respuestasPrimeraFila, (height / 2) + 70);
            doc.text($scope.properties.PDFobjPadre.nombre, respuestasFilaIntermedia, (height / 2) + 70);
            doc.text($scope.properties.PDFobjPadre.apellidos, respuestasSegundaFila, (height / 2) + 70);
        } else {
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 70);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 70);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 70);
        }

        if ($scope.properties.PDFobjPadre.vive != null) {
            if ($scope.properties.PDFobjPadre.vive.descripcion == "Sí") {
                doc.text($scope.properties.PDFobjPadre.correoElectronico, respuestasPrimeraFila, (height / 2) + 82);
                doc.text($scope.properties.PDFobjPadreCatEscolaridad.descripcion, respuestasFilaIntermedia, (height / 2) + 82);
                doc.text(($scope.properties.PDFobjPadreCatCampusEgreso == null ? "No" : $scope.properties.PDFobjPadreCatCampusEgreso.descripcion), respuestasSegundaFila, (height / 2) + 82);

                doc.text(($scope.properties.PDFobjPadre.puesto == null || $scope.properties.PDFobjPadre.puesto == "" ? "No trabaja" : $scope.properties.PDFobjPadre.puesto), respuestasPrimeraFila, (height / 2) + 94);
                doc.text(($scope.properties.PDFobjPadre.empresaTrabaja == null || $scope.properties.PDFobjPadre.empresaTrabaja == "" ? "No trabaja" : $scope.properties.PDFobjPadre.empresaTrabaja), respuestasFilaIntermedia, (height / 2) + 94);
            } else {
                doc.setFont(undefined, 'normal');
                doc.text("Finado", respuestasPrimeraFila, (height / 2) + 82);
                doc.text("Finado", respuestasFilaIntermedia, (height / 2) + 82);
                doc.text("Finado", respuestasSegundaFila, (height / 2) + 82);

                doc.text("Finado", respuestasPrimeraFila, (height / 2) + 94);
                doc.text("Finado", respuestasFilaIntermedia, (height / 2) + 94);
            }
        } else {
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 82);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 82);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 82);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 94);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 94);
        }

        doc.addPage();

        //  ----------------------------------- NUEVA HOJA Y SEPTIMA SECCIÓN  ----------------------------------- 

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 116, 'Domicilio permanente del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 106, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) - 106, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) - 106, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) - 90.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) - 93, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) - 88, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) - 90.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) - 76, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) - 76, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) - 76, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) - 64, 'Teléfono:');

        //Respuestas
        if ($scope.properties.PDFobjPadre.catPais != null) {
            doc.setFont(undefined, 'normal');
            doc.text($scope.properties.PDFobjPadre.catPais.descripcion, respuestasPrimeraFila, (height / 2) - 101);
            doc.text($scope.properties.PDFobjPadre.codigoPostal, respuestasFilaIntermedia, (height / 2) - 101);
            doc.text(($scope.properties.PDFobjPadre?.catEstado?.descripcion == null || $scope.properties.PDFobjPadre?.catEstado?.descripcion == "" ? $scope.properties.PDFobjPadre?.estadoExtranjero : $scope.properties.PDFobjPadre?.catEstado?.descripcion), respuestasSegundaFila, (height / 2) - 101);

            doc.text($scope.properties.PDFobjPadre.ciudad, respuestasPrimeraFila, (height / 2) - 86.5);
            doc.text($scope.properties.PDFobjPadre.delegacionMunicipio, respuestasFilaIntermedia, (height / 2) - 83);
            doc.text($scope.properties.PDFobjPadre.colonia, respuestasSegundaFila, (height / 2) - 86.5);

            doc.text($scope.properties.PDFobjPadre.calle, respuestasPrimeraFila, (height / 2) - 71);
            doc.text(( $scope.properties.PDFobjPadre.numeroExterior == null || $scope.properties.PDFobjPadre.numeroExterior == "" ? "N/A" : $scope.properties.PDFobjPadre.numeroExterior), respuestasFilaIntermedia, (height / 2) - 71);
            doc.text(( $scope.properties.PDFobjPadre.numeroInterior == null || $scope.properties.PDFobjPadre.numeroInterior == "" ? "N/A" : $scope.properties.PDFobjPadre.numeroInterior), respuestasSegundaFila, (height / 2) - 71);
            doc.text($scope.properties.PDFobjPadre.telefono, respuestasPrimeraFila, (height / 2) - 59);

        } else {

            doc.setFont(undefined, 'normal');
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 101);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 101);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 101);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 86.5);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 83);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 86.5);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 71);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 71);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 71);
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 59);
        }

        //  ----------------------------------------- OCTAVA SECCIÓN  ----------------------------------------- 

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 51, 190, 120, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 41, 'Información de la madre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 31, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) - 31, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) - 31, 'Apellido (s):');

        doc.text(margenPrimeraFila, (height / 2) - 19, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) - 19, 'Escolaridad del tutor:');
        doc.text(margenSegundaFila, (height / 2) - 19, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) - 7, 'Ocupación del tutor:');
        doc.text(margenFilaIntermedia, (height / 2) - 7, 'Empresa:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        if ($scope.properties.PDFobjMadreCatTitulo != null) {
            doc.text($scope.properties.PDFobjMadreCatTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 26);
            doc.text($scope.properties.PDFobjMadre.nombre, respuestasFilaIntermedia, (height / 2) - 26);
            doc.text($scope.properties.PDFobjMadre.apellidos, respuestasSegundaFila, (height / 2) - 26);

        } else {
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 26);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 26);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 26);
        }

        if ($scope.properties.PDFobjMadre.vive != null) {
            if ($scope.properties.PDFobjMadre.vive.descripcion == "Sí") {
                doc.text($scope.properties.PDFobjMadre.correoElectronico, respuestasPrimeraFila, (height / 2) - 14);
                doc.text($scope.properties.PDFobjMadreCatEscolaridad.descripcion, respuestasFilaIntermedia, (height / 2) - 14);
                doc.text(($scope.properties.PDFobjMadreCatCampusEgreso == null ? "No" : $scope.properties.PDFobjMadreCatCampusEgreso.descripcion), respuestasSegundaFila, (height / 2) - 14);

                doc.text(($scope.properties.PDFobjMadre.puesto == null || $scope.properties.PDFobjMadre.puesto == "" ? "No trabaja" : $scope.properties.PDFobjMadre.puesto), respuestasPrimeraFila, (height / 2) - 2);
                doc.text(($scope.properties.PDFobjMadre.empresaTrabaja == null || $scope.properties.PDFobjMadre.empresaTrabaja == "" ? "No trabaja" : $scope.properties.PDFobjMadre.empresaTrabaja), respuestasFilaIntermedia, (height / 2) - 2);
            } else {
                doc.setFont(undefined, 'normal');
                doc.text("Finado", respuestasPrimeraFila, (height / 2) - 14);
                doc.text("Finado", respuestasFilaIntermedia, (height / 2) - 14);
                doc.text("Finado", respuestasSegundaFila, (height / 2) - 14);

                doc.text("Finado", respuestasPrimeraFila, (height / 2) - 2);
                doc.text("Finado", respuestasFilaIntermedia, (height / 2) - 2);
            }

        } else {
            doc.setFont(undefined, 'normal');
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 14);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 14);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 14);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 2);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 2);

        }

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 8, 'Domicilio permanente de la madre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 18, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) + 18, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) + 18, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 31.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 30, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 35, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 31.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 47, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 47, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) + 47, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) + 59, 'Teléfono:');

        //Respuestas
        if ($scope.properties.PDFobjMadre.catPais != null) {
            doc.setFont(undefined, 'normal');
            doc.text($scope.properties.PDFobjMadre.catPais.descripcion, respuestasPrimeraFila, (height / 2) + 23);
            doc.text($scope.properties.PDFobjMadre.codigoPostal, respuestasFilaIntermedia, (height / 2) + 23);
            doc.text(($scope.properties.PDFobjMadre?.catEstado?.descripcion == null || $scope.properties.PDFobjMadre?.catEstado?.descripcion == "" ? $scope.properties.PDFobjMadre?.estadoExtranjero : $scope.properties.PDFobjPadre?.PDFobjMadre?.descripcion), respuestasSegundaFila, (height / 2) + 23);

            doc.text($scope.properties.PDFobjMadre.ciudad, respuestasPrimeraFila, (height / 2) + 35.5);
            doc.text($scope.properties.PDFobjMadre.delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 40);
            doc.text($scope.properties.PDFobjMadre.colonia, respuestasSegundaFila, (height / 2) + 35.5);

            doc.text($scope.properties.PDFobjMadre.calle, respuestasPrimeraFila, (height / 2) + 52);
            doc.text(($scope.properties.PDFobjMadre.numeroExterior == null || $scope.properties.PDFobjMadre.numeroExterior == "" ? "N/A" : $scope.properties.PDFobjMadre.numeroExterior), respuestasFilaIntermedia, (height / 2) + 52);
            doc.text(($scope.properties.PDFobjMadre.numeroInterior == null || $scope.properties.PDFobjMadre.numeroInterior == "" ? "N/A" : $scope.properties.PDFobjMadre.numeroInterior), respuestasSegundaFila, (height / 2) + 52);
            doc.text($scope.properties.PDFobjMadre.telefono, respuestasPrimeraFila, (height / 2) + 64);
        } else {

            doc.setFont(undefined, 'normal');
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 23);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 23);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 23);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 35.5);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 40);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 35.5);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 52);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 52);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 52);
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 64);

        }


        //  ----------------------------------------- NOVENA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 79, 'Contacto(s) en caso de emergencia');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 89, 'Contacto de emergencia:');
        doc.text(margenFilaIntermedia, (height / 2) + 89, 'Parentesco:');
        doc.text(margenSegundaFila, (height / 2) + 89, 'Teléfono de emergencia:');

        doc.text(margenPrimeraFila, (height / 2) + 101, 'Celular de emergencia:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].nombre, respuestasPrimeraFila, (height / 2) + 94);
        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].parentesco, respuestasFilaIntermedia, (height / 2) + 94);
        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].telefono, respuestasSegundaFila, (height / 2) + 94);

        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].telefonoCelular, respuestasPrimeraFila, (height / 2) + 106);

        doc.addPage();
        //funcion para centrar el texto
        var centeredText = function(text, y) {
            var textWidth = doc.getStringUnitWidth(text) * doc.internal.getFontSize() / doc.internal.scaleFactor;
            var textOffset = (doc.internal.pageSize.width - textWidth) / 2;
            doc.text(textOffset, y, text);
        }
        let yValidacion = 15;
        doc.setTextColor('#ff5900')
        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        centeredText("Validar y envía tu información", yValidacion)
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'normal');
        doc.text( "Si has revisado cuidadosamente tu información, si es verídica y correcta, es momento de enviar tu solicitud.", respuestasPrimeraFila, yValidacion+=10);
        doc.text( "Nuestro equipo de Admisiones te apoyará en este proceso y te acompañará para mantenerte al tanto de tu solicitud.", respuestasPrimeraFila, yValidacion+=10);
        doc.text( "¡Te deseamos mucha suerte!", respuestasPrimeraFila, yValidacion+=10);
        
        doc.setTextColor(0);

        doc.text( "Confirmo que los datos aquí presentes son verídicos y poseo toda la documentación necesaria para respaldarlos.", respuestasPrimeraFila, yValidacion+=10);
        doc.text( "Estoy de acuerdo y he leído el Aviso de privacidad", respuestasPrimeraFila, yValidacion+=10);
        doc.text( "Confirmo que los datos aquí presentes son verídicos y poseo toda la documentación necesaria para respaldarlos.", respuestasPrimeraFila, yValidacion+=10);



        

        doc.save(`Solicitud_de_Admision.pdf`);
    }

   /* var cheackbox = function(){
        doc.text('CheckBox:', 10, 125);
        var checkBox = new CheckBox();
        checkBox.fieldName = "CheckBox1";
        checkBox.Rect = [50, 120, 30, 10];
        checkBox.value = 'Yes'
        doc.addField(checkBox);
    }*/

}