function PbUploadCtrl($scope, $sce, $element, widgetNameFactory, $timeout, $log, gettextCatalog) {
    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';

    this.clear = clear;
    this.startUploading = startUploading;
    this.uploadError = uploadError;
    this.uploadComplete = uploadComplete;

    this.name = widgetNameFactory.getName('pbUpload');

    this.preventFocus = function($event) {
        $event.target.blur();
    };

    this.submitForm = function() {
        console.log("submitForm")
        var fileUpload = document.getElementById("file");
        $scope.properties.revisar = false;
        $scope.properties.permitido = false;
        var form = $element.find('form');
        form.triggerHandler('submit');
        var fileUpload = document.getElementById("file");
        form[0].submit();
    };

    this.forceSubmit = function(event) {
        console.log("forceSubmit")
        if (!event.target.value) {
            return;
        }
        ctrl.submitForm();
        //event.target.value = null;
    };

    var input = $element.find('input');
    input.on('change', ctrl.forceSubmit);
    $scope.$on('$destroy', function() {
        input.off('change', ctrl.forceSubmit);
    });

    $scope.$watch('properties.url', function(newUrl, oldUrl) {
        ctrl.url = $sce.trustAsResourceUrl(newUrl);
        if (newUrl === undefined) {
            $log.warn('you need to define a url for pbUpload');
        }
    });

    //the filename displayed is not bound to the value as a bidirectionnal
    //bond, thus, in case the value is updated, it is not reflected
    //to the filename (example with the BS-14498)
    //we watch the value to update the filename and the upload widget state
    $scope.$watch(function() {
        console.log("new value")
        return $scope.properties.value;
    }, function(newValue) {
        var fileUpload = document.getElementById("file");
        if (newValue && newValue.filename) {
            ctrl.filemodel = true;
            ctrl.filename = newValue.filename;
        } else if (!angular.isDefined(newValue)) {
            delete ctrl.filemodel;
            delete ctrl.filename;
        }
    });

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbUpload property named "value" need to be bound to a variable');
    }

    function clear() {
        ctrl.filename = '';
        ctrl.filemodel = '';
        $scope.properties.value = {};
        event.target.value = null;
        $scope.properties.urlretorno = "";
        $scope.$apply();
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
        var fileUpload = document.getElementById("file");
        //when the upload widget return a String, it means an error has occurred (with a html document as a response)
        //if it's not a string, we test if it contains some error message
        if (angular.isString(response) || (response && response.type && response.message)) {
            $log.warn('upload failed');
            ctrl.filemodel = '';
            ctrl.filename = gettextCatalog.getString('Upload failed');
            $scope.properties.errorContent = angular.isString(response) ? response : response.message;
            return;
        }
        // $scope.properties.value = response;
        $scope.CheckDimension(response);
    }


    $scope.CheckDimension = function(response) {
        debugger;
        //Get reference of File.
        var fileUpload = document.getElementById("file");
        $scope.properties.revisar = true;
        //Check whether the file is valid Image.
        //var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.png|.gif)$");
        var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.png|.jpeg)$");
        // if (regex.test($scope.properties.value.filename.toLowerCase())) {
        if (regex.test(response.filename.toLowerCase())) {
            //Check whether HTML5 is supported.
            if (typeof(fileUpload.files) != "undefined") {
                //Initiate the FileReader object.
                var reader = new FileReader();
                //Read the contents of Image File.
                reader.readAsDataURL(fileUpload.files[0]);
                reader.onload = function(e) {
                    // valido = true;
                    //Initiate the JavaScript Image object.
                    var image = new Image();
                    //Set the Base64 string return from FileReader as source.
                    image.src = e.target.result;
                    //Validate the File Height and Width.
                    image.onload = function() {
                        var height = this.height;
                        var width = this.width;
                        if (height < $scope.properties.HeightMaximo || width < $scope.properties.WidthMaximo) {
                            //show width and height to user
                            /*document.getElementById("width").innerHTML=width;
                            document.getElementById("height").innerHTML=height;*/
                            ctrl.clear()
                            $scope.properties.permitido = false;
                            /*$scope.properties.value = {};
                            ctrl.filemodel = {}*/
                            /*alert("HEIGHT" + height + " , " + "WIDTH" + width)
                            alert("Height NO PUEDE EXCEDER " + $scope.properties.HeightMaximo + " Y EL WIDTH NO PUEDE EXCEDER" + $scope.properties.WidthMaximo);*/
                            swal("¡La resolución de la imagen debe ser mayor de 480 x 576px!", "La imagen que intenta cargar es de " + height + " x " + width + "px. " + "Favor de cargar una imagen con la resolución indicada.", "warning");
                            return false;
                            
                            /*
                            swal({title: 'La imagen debe ser mayor de 480 x 576!', text: 'Mensaje de texto', html: '"Tu imagen es de " + height + "x " + width + "<br> Por favor intenta con la foto original, que no sea descargada de alguna red social."', type: 'warning'});
                            */
                        }

                        $scope.properties.permitido = true;
                        $scope.properties.revisar = false;
                        // alert("LA IMAGEN TIENE HEIGHT Y WIDTH DENTRO DEL MARGEN.");
                        $scope.properties.value = response;
                        $scope.properties.urlretorno = URL.createObjectURL(fileUpload.files[0]);
                        $scope.$apply();
                        return true;
                    };

                }
                // if(!valido){
                //     swal("Advertencia", "Ya has agregado esta imagen y no cumple con los requisitos mínimos favor de agregar una diferente.", "warning");
                //     ctrl.clear()
                // }
            } else {
                ctrl.clear()
                // alert("This browser does not support HTML5.");
                swal("¡HTML5!", "Este navegador no soporta HTML5", "warning");
                return false;
            }
        } else {
            ctrl.clear()
            // alert("POR FAVOR SELECCIONE UNA IMAGEN VALIDA.");
            swal("¡Formato Incorrecto!", "Solo se aceptan imagenes png, jpg, jpeg", "warning");
            return false;
        }
    }


}