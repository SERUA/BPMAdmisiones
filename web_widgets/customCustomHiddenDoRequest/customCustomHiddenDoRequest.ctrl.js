function($scope, $http) {

    $scope.textTranslate = "";
    $scope.titleTranslate = "";
    $scope.resultado = "";
    $scope.arrayTranslate = [];
    $scope.positionArray = "";
    $scope.idioma = localStorage.getItem("idioma");

    function doRequest(method, url, params) {
        var req = {
            method: "GET",
            url: $scope.properties.url,
            data: {}
        };

        return $http(req).success(function(data, status) {
                $scope.properties.successResponseValue = data;
            })
            .error(function(data, status) {
                $scope.properties.errorResponseValue = data;
                if (data.error === "fallo por Cannot invoke method size() on null object") {
                    //swal("Activado", "El usuario ya se encuentra activado", "success");
                    $scope.readJson("Activado!", "¡El usuario ya se encuentra activado!");
                    swal({
                        title: $scope.titleTranslate,
                        text: $scope.textTranslate,
                        type: "success",
                        timer: 13000,
                        closeOnClickOutside: false,
                        buttons: false
                    });
                    setTimeout(fun, 2000);


                } else {
                    swal("Error", data.error, "error");
                }
            })
            .finally(function() {
                vm.busy = false;
                $scope.hideLoading();
            });
    }

    $scope.$watch("properties.url", function() {
        if ($scope.properties.url !== undefined && $scope.properties.url !== "") {
            doRequest();
        }
    });


    function fun() {
        //window.location = "http://www.youtube.com";
        window.location.assign("https://" + location.host + "/apps/login")
    }

    $scope.readJson = function(titulo, mensaje) {
        var strJson = JSON.stringify(jsonIdioma);
        var objJson = JSON.parse(strJson);
        var idiomaTemp = "";
        var i = 0;

        if ($scope.idioma == "ESP" ? idiomaTemp = "es-ES" : idiomaTemp = "en-EN")

            if (idiomaTemp == "en-EN") {
            $scope.arrayTranslate.push(titulo);
            $scope.arrayTranslate.push(mensaje);

            for (i = 0; i < $scope.arrayTranslate.length; i++) {
                $scope.positionArray = $scope.arrayTranslate[i];
                $scope.resultado = objJson[idiomaTemp][$scope.positionArray];
                switch (i) {
                    case 0:
                        $scope.titleTranslate = $scope.resultado;
                        break;
                    case 1:
                        $scope.textTranslate = $scope.resultado;
                        break;
                    default:
                        console.log("Error, en el ciclo");
                }
            }
        } else if (idiomaTemp == "es-ES") {
            $scope.titleTranslate = titulo;
            $scope.textTranslate = mensaje;
        } else {
            console.log("Error, idiomas no detectado");
        }

    }
}