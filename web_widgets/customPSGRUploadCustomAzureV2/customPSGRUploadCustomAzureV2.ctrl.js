function PbUploadCtrl($scope, $sce, $element, widgetNameFactory, $timeout, $log, gettextCatalog, $http, modalService) {
    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';
    $scope.fileSelectModel = false;

    $scope.documetObject = {
        "b64": "",
        "filename": "",
        "filetype": "",
        "contenedor": ""
    }

    this.clear = clear;
    this.startUploading = startUploading;
    this.uploadError = uploadError;
    this.uploadComplete = uploadComplete;

    this.name = widgetNameFactory.getName('pbUpload');

    this.preventFocus = function ($event) {
        $event.target.blur();
    };

    this.submitForm = function () {
        // var form = $element.find('form');
        // form.triggerHandler('submit');
        // form[0].submit();
    };


    $scope.downloadFile = function(){
        var req = {
            method: "GET",
            url: $scope.properties.urlDownloadFile + $scope.properties.idDocumento
        };

        return $http(req)
        .success(function (data, status) {
            downloadFile(data[0]);
        })
        .error(function (data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            
        });
    };
    
    function downloadFile(_document) {
        let urlSplitted = _document.urlAzure.split("/");
        const linkSource = _document.b64;
        const downloadLink = document.createElement("a");
        const fileName = urlSplitted[urlSplitted.length - 1] + "." + _document.extension;
    
        downloadLink.href = linkSource;
        downloadLink.download = fileName;
        downloadLink.click();
    }

    function downloadFEjemplo(_document) {
        const linkSource = _document.b64;
        const downloadLink = document.createElement("a");
    
        let fileName = $scope.properties.documentName + _document.extension;

        downloadLink.href = linkSource;
        downloadLink.download = fileName;
        downloadLink.click();
    }

    function handleFileSelect(evt) {
        startUploading();
        var f = evt.target.files[0];
        var reader = new FileReader(); var size = parseFloat(f.size / 1024).toFixed(2);
        
        if(size >= 2000){
            ctrl.filename = gettextCatalog.getString('Error al subir documento');
            swal("El archivo es demasiado grande", "El tamaño máximo de la imagen es de 2MB", "error");
        } else {
            reader.onload = (function (theFile) {
                return function (e) {
                    var binaryData = e.target.result;
                    var base64String = window.btoa(binaryData);
                    $scope.documetObject["b64"] = $scope.documetObject["filetype"] +  "," +  base64String;
                    
                    doRequest("POST", "../API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=0", null);
                };
            })(f);
            reader.readAsBinaryString(f);
        }
    }

    function handleFileSelectImg(evt) {
        startUploading();
        var f = evt.target.files[0];
        var reader = new FileReader();
        var reader2 = new FileReader();
        reader.onload = function(e){
            var binaryData = e.target.result;
            var base64String = window.btoa(binaryData);
            $scope.documetObject["b64"] = $scope.documetObject["filetype"] +  "," +  base64String;
            doRequest("POST", "../API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=0", null);
        };

        reader2.onload = function(e){
            var image = new Image();
            image.onload = function(){
                reader.readAsBinaryString(f);
                //width >= 1024 && height >= 768
                /*
                var height = image.height;
                var width = image.width;
                if(width >= 460 && height >= 460){
                    reader.readAsBinaryString(f);
                }  else {
                    // swal("No se pudo cargar la imagen","La imagen es muy pequeña, esta debe ser por  1024 x 768 pixeles. ","error")
                    let swalObject = {
                        title: "",
                        icon:"warning",
                        text: "Estás intentando subir una foto que no cumple con las recomendaciones (tamaño minimo recomendado de 460 x 460). ¿Deseas continuar?",
                        buttons: {
                            no: {
                                text: "No",
                                value: false,
                                className:'btn-info'
                            },
                            si: {
                                text: "Sí",
                                value: true,
                                className:'btn-primary'
                            }
                        }
                    };

                    swal(swalObject).then((value) => {
                        if(value){
                            reader.readAsBinaryString(f);
                        }
                    });
                }*/
            }

            image.src = e.target.result;
        }
        reader2.readAsDataURL(f);
    }


    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.documetObject),
            params: params
        };

        return $http(req)
        .success(function (data, status) {
            $scope.properties.urlAzure = angular.copy(data.data[0]);
            uploadComplete(data);
        })
        .error(function (data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            
        });
    }
    
    $scope.openModalFile = function(){
        
        selectFile();
        openModal($scope.properties.idModal);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    function closeModal(shouldClose) {
        if(shouldClose){
            modalService.close();
        }
    }

    /**
    * Execute a get/post request to an URL
    * It also bind custom data from success|error to a data
    * @return {void}
    */
    function getFile() {
        var req = {
            method: "GET",
            url: $scope.properties.urlDownloadFile,
        };

        return $http(req)
        .success(function (data, status) {
            console.log(data);
        })
        .error(function (data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            
        });
    }

    this.forceSubmit = function (event) {
        $scope.procesar = false;
        let filename = event.target.files[0].name 
        let file_splited = filename.split(".");
        let ext = "";
        
        if (file_splited.length > 1) {
            ext = file_splited[file_splited.length - 1]
        }
        
        let name_without_ext = filename.replace("." + ext, "");
        let extension = ext ? "." + ext : "";

        $scope.documetObject["filename"] = $scope.properties.caseid + "/" + name_without_ext + "_" + new Date().getTime() + extension;
        $scope.documetObject["filetype"] = event.target.files[0].type;
        $scope.documetObject["contenedor"] = "privado";
        
        let errorMsg = "";
        $scope.properties.isImagen = "false";
        $scope.properties.isPDF = "false";
        $scope.properties.isXML = "false";
        $scope.procesar = false;
        $scope.properties.urlretorno = "";
        switch ($scope.properties.tipoDocumento) {
            case "pdf": 
                if (event.target.files[0].type === "application/pdf") {
                    $scope.properties.isPDF = "true";
                    $scope.procesar = true;
                    $scope.fileSelectModel = true;
                    handleFileSelect(event);
                }
                else errorMsg = "Solo puede agregar archivos PDF";
                break;
            case "imagen": 
                if (event.target.files[0].type === "image/jpeg" || event.target.files[0].type === "image/png") {
                    $scope.properties.isImagen = "true";
                    $scope.procesar = true;
                    $scope.fileSelectModel = true;
                    handleFileSelectImg(event);
                }
                else errorMsg = "Solo puede agregar imagenes PNG o JPEG";
                break;
            case "xml": 
                if (event.target.files[0].type === "application/xml") {
                    $scope.properties.isXML = "true";
                    $scope.procesar = true;
                    $scope.fileSelectModel = true;
                    //handleFileSelectImg(event);
                }
                else errorMsg = "Solo puede agregar archivos XML";
                break;
            default:
                break;
        }
        /*
        if (event.target.files[0].type === "image/jpeg") {
            $scope.properties.isImagen = "true";
            $scope.properties.isPDF = "false";
            $scope.procesar = true;
            if($scope.properties.tipoDocumento === "pdf"){
                swal("!Formato no valido!", "Solo puede agregar archivos PDF o imagenes JPG y PNG", "warning");
                $scope.properties.urlretorno = "";
                handleFileSelect(event);
            } else {
                handleFileSelectImg(event);
            }
        } else if (event.target.files[0].type === "image/png") {
            $scope.properties.isImagen = "true";
            $scope.properties.isPDF = "false";
            $scope.procesar = true;
            if($scope.properties.tipoDocumento === "pdf"){
                handleFileSelect(event);
            } else {
                handleFileSelectImg(event);
            }
        } else if (event.target.files[0].type === "application/pdf") {
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "false";
            $scope.procesar = true;
            handleFileSelect(event);
        } else {
            swal("!Formato no valido!", "Solo puede agregar archivos PDF o imagenes JPG y PNG", "warning");
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "true";
            $scope.properties.urlretorno = "";
        }*/

        if (errorMsg) {
            swal("!Formato no valido!", errorMsg, "warning");
        }

        if ($scope.procesar === true) {
            $scope.properties.urlretorno = URL.createObjectURL(event.target.files[0]);

            if (!event.target.value) {
                return;
            }
            ctrl.submitForm();
            event.target.value = null;
        }

    };

    var input = $element.find('input');
    input.on('change', ctrl.forceSubmit);
    $scope.$on('$destroy', function () {
        input.off('change', ctrl.forceSubmit);
    });

    $scope.$watch('properties.url', function (newUrl, oldUrl) {
        ctrl.url = $sce.trustAsResourceUrl(newUrl);
        if (newUrl === undefined) {
            $log.warn('you need to define a url for pbUpload');
        }
    });

    $scope.$watch(function () { return $scope.properties.value; }, function (newValue) {
        if (newValue && newValue.filename) {
            ctrl.filemodel = true;
            ctrl.filename = newValue.filename;
        } else if (!angular.isDefined(newValue)) {
            delete ctrl.filemodel;
            delete ctrl.filename;
        } else {
            $scope.properties.urlretorno = "";
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "true";
        }
    });

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbUpload property named "value" need to be bound to a variable');
    }

    function clear() {
        ctrl.filename = '';
        ctrl.filemodel = '';
        $scope.properties.value = {};
        $scope.linkSource  = '';
        $scope.fileName  = '';
        $scope.extension  = '';
        $scope.properties.urlAzure = null;
        $scope.fileSelectModel = false;
        
        let obj = {
            "linkSource": "",
            "fileName":  "",
            "extension": ""
        };
        
        $scope.properties.selectedFile = obj;
    }

    function uploadError(error) {
        $log.warn('upload fails too', error);
        ctrl.filemodel = '';
        $scope.fileSelectModel = false;
        ctrl.filename = gettextCatalog.getString('Upload failed');
    }

    function startUploading() {
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Uploading...');
    }

    function uploadComplete(response) {
        $scope.properties.value = angular.copy(response);
    }

    $scope.$watch("properties.urlAzure", (_new)=>{
        if(_new){
            let array = _new.split("/");
            ctrl.filename = array[array.length - 1];
            this.uploadComplete = true;
            $scope.downloadFile2();
            $scope.fileSelectModel = true;
        } 
        else {
            clear()
        }
    });

    $scope.downloadFile2 = function(){
        var req = {
            method: "GET",
            url: $scope.properties.urlDownloadFile + $scope.properties.urlAzure
        };

        return $http(req)
        .success(function (data, status) {
            if(data.data){
                downloadFile2(data.data[0]);
            } else {
                downloadFile2(data[0]);
            }
        })
        .error(function (data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            
        });
    };

    $scope.descargarEjemplo = function(){
        var req = {
            method: "GET",
            url: $scope.properties.urlDownloadFile + $scope.properties.urlEjemplo
        };

        return $http(req)
        .success(function (data, status) {
            if(data.data){
                downloadFEjemplo(data.data[0]);
            } else {
                downloadFEjemplo(data[0]);
            }
        })
        .error(function (data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            
        });
    };

    function downloadFile2(_document) {
        let urlSplitted = $scope.properties.urlAzure.split("/");
        $scope.linkSource = _document.b64;
        $scope.fileName = urlSplitted[urlSplitted.length - 1];
        $scope.extension = _document.extension;
    }

    function selectFile(){
        let obj = {
            "linkSource":$scope.linkSource,
            "fileName":  $scope.fileName ,
            "extension": $scope.extension
        };
        
        $scope.properties.selectedFile = obj;
    }
}