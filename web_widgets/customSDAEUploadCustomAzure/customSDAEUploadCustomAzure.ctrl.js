function PbUploadCtrl($scope, $sce, $element, widgetNameFactory, $timeout, $log, gettextCatalog, $http, modalService) {
    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';

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

    function handleFileSelect(evt) {
        startUploading();
        var f = evt.target.files[0];
        var reader = new FileReader();
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
            $scope.properties.urlAzure = data.data[0];
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
        handleFileSelect(event);
        $scope.documetObject["filename"] = event.target.files[0].name;
        $scope.documetObject["filetype"] = event.target.files[0].type;
        $scope.documetObject["contenedor"] = "privado";

        if (event.target.files[0].type === "image/jpeg") {
            $scope.properties.isImagen = "true";
            $scope.properties.isPDF = "false";
            $scope.procesar = true;
        } else if (event.target.files[0].type === "image/png") {
            $scope.properties.isImagen = "true";
            $scope.properties.isPDF = "false";
            $scope.procesar = true;
        } else if (event.target.files[0].type === "application/pdf") {
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "false";
            $scope.procesar = true;
        } else {
            swal("!Formato no valido!", "Solo puede agregar archivos PDF o imagenes JPG y PNG", "warning");
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "true";
            $scope.properties.urlretorno = "";
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
    }

    function uploadError(error) {
        $log.warn('upload fails too', error);
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Upload failed');
    }

    function startUploading() {
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Uploading...');
    }

    function uploadComplete(response) {
        // if (angular.isString(response) || (response && response.type && response.message)) {
        //     $log.warn('upload failed');
        //     ctrl.filemodel = '';
        //     ctrl.filename = gettextCatalog.getString('Upload failed');
        //     $scope.properties.errorContent = angular.isString(response) ? response : response.message;
        //     return;
        // } else {
        //     let array = response.data[0].split("/");
        //     ctrl.filename = array[array.length - 1];
        //     this.uploadComplete = true;
        // }
        $scope.properties.value = response;
    }

    $scope.$watch("properties.urlAzure", (_new)=>{
        if(_new){
            let array = _new.split("/");
            ctrl.filename = array[array.length - 1];
            this.uploadComplete = true;
            $scope.downloadFile2();
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

    function downloadFile2(_document) {
        debugger;
        let urlSplitted = $scope.properties.urlAzure.split("/");
        $scope.linkSource = _document.b64;
        $scope.fileName = urlSplitted[urlSplitted.length - 1];
        $scope.extension = _document.extension;
        
        $scope.properties.selectedFile = {
            "linkSource":$scope.linkSource,
            "fileName":  $scope.fileName ,
            "extension": $scope.extension
        };
    }
}