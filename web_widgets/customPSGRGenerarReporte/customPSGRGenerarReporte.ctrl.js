function PbImageButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;
    this.action = function action() {
        debugger
        if ($scope.properties.disabled) return;
        
        var url = "";
        
        if ($scope.properties.reporte === "General") {
            url = "../API/extension/posgradosRest?url=getExcelFileGeneralProceso&p=0&c=999";
        }
        
        if (url) {
            doRequest("POST", url, null, $scope.properties.dataToSend, function(data) {
                if ($scope.properties.fileExtension === "xls") {
                    const blob = b64toBlob(data.data[0]);
                    const blobUrl = URL.createObjectURL(blob);
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    let currentdate = new Date(); 
                    let datetime = $scope.properties.reporte + " - "+ currentdate.getDate() + "/"
                    + (currentdate.getMonth()+1)  + "/" 
                    + currentdate.getFullYear() 
                    link.download = datetime;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                    //window.location = blobUrl;
                }
                else {
                    //fakeLink(data.data[1])
                }
            })   
        }
        else{
            Swal.fire({
                icon: 'info',
                title: 'No hay reporte seleccionado',
            })  
        }
        
    }

    function b64toBlob(dataURI) {
        console.log("mensaje")
        var byteString = atob(dataURI);
        var ab = new ArrayBuffer(byteString.length);
        var ia = new Uint8Array(ab);
        let contentType = "text/plain";
        if ($scope.properties.fileExtension === "xls") {
            contentType = "application/vnd.ms-excel";
        }else if($scope.properties.fileExtension === "csv"){
            contentType = "text/csv";
        }
        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }
        return new Blob([ab], { type: contentType });
    }

    function fakeLink(rua) {

        const linkSource = `data:text/plain;base64,${rua}`;
        const downloadLink = document.createElement("a");
        var fileName = "kwafile.rua";
        var descargo = false;
        for (let index = 0; index < $scope.properties.lstSesiones.length; index++) {
            const element = $scope.properties.lstSesiones[index];
            try {
                if(!descargo && !isNullOrUndefined($scope.properties.dataToSend.idbanner) && $scope.properties.dataToSend.idbanner.length == 8 && isNullOrUndefined($scope.properties.dataToSend.sesion)){
                    descargo = true;
                    fileName = $scope.properties.dataToSend.idbanner + ".rua";
                    downloadLink.href = linkSource;
                    downloadLink.download = fileName;
                    downloadLink.click();
                } else if (element.id == $scope.properties.ruaname.split(',')[0]) {
                    fileName = element.text + ".rua";
                    downloadLink.href = linkSource;
                    downloadLink.download = fileName;
                    downloadLink.click();
                }
            } catch (e) {
                if(! descargo){
                    Swal.fire({
                        icon: 'info',
                        title: 'Sin resultados',
                        text: 'Favor de seleccionar por lo menos una sesión'
                    })  
                }
                
            }


        }

    }
    
    function isNullOrUndefined(dato) {
        if (dato === undefined || dato === null || dato.toString().trim().length <= 0) {
            return true;
        }
        return false
    }
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data);
            })
            .error(function(data, status) {
                console.error(data)
                Swal.fire({
                    icon: 'info',
                    title: 'Sin resultados',
                    text: 'No se encontraron resultados para la búsqueda'
                })
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function redirectIfNeeded() {
        var iframeId = $window.frameElement ? $window.frameElement.id : null;
        //Redirect only if we are not in the portal or a living app
        if (!iframeId || iframeId && iframeId.indexOf('bonitaframe') !== 0) {
            $window.location.assign($scope.properties.targetUrlOnSuccess);
        }
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return { 'user': userId };
        }
        return {};
    }


    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }


}