function($scope, $http) {
    var vm = this;
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
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data)
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
            });
    }
    
    $scope.previsualizar = function() {
        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=generateHtmlSDAE&p=0&c=10", null, $scope.properties.datosPrevisualizar, function(datos) {
            console.log(document.getElementById($scope.properties.id).innerHTML);
            var respuesta = datos.data[0].replace($scope.properties.replace, document.getElementById($scope.properties.id).innerHTML);
            Swal.fire({
                html: respuesta,
                showCloseButton: false,
                width: 800,
                showCancelButton: false,
                focusConfirm: false,
                confirmButtonColor: "#333",
                confirmButtonText: 'Cerrar'
            })
            /*$("#modal" + $scope.properties.id).show();
            setTimeout(function() {
                var element = document.getElementById("div" + $scope.properties.id);

                element.innerHTML = respuesta

            }, 100);*/

        });
    }
    
    var hidden = document.getElementsByClassName("oculto");
    hidden[0].classList.add("hidden");
}