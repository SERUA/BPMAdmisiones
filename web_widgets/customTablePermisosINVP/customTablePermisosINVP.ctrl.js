function PbTableCtrl($scope, blockUI, $http) {
    $scope.filtro = "";
    $scope.seleccionado = false;
    var vm = this;
    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function(row) {
            return angular.equals(row, $scope.properties.selectedRow);
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
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data);

            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
            });
    }
    //bonita/API/identity/role?p=0&c=10&o=displayName%20ASC}
    $scope.lstRoles = [];
    $scope.pagina = {};
    $scope.rolSelected = {};
    $scope.setRol = function(rolSelected) {
        $scope.rolSelected = rolSelected;
    }
    $scope.paginaSelected = function(row) {
        $scope.pagina = row;
        $scope.seleccionado = true;
        doRequest("GET", "/bonita/API/identity/role?p=0&c=999&o=displayName%20ASC", null, null, function(datos) {
            $scope.lstRoles = [];
            for (let index = 0; index < datos.length; index++) {
                $scope.lstRoles.push({...datos[index], "nuevo": true, "eliminado": false })
            }
        })
    }
    $scope.agregarRol = function() {

        var agregado = false;
        for (let index = 0; index < $scope.pagina.roles.length; index++) {
            const element = $scope.pagina.roles[index];
            if (element.id == $scope.rolSelected.id && element.eliminado == false) {
                agregado = true;
                break;
            } else if (element.id == $scope.rolSelected.id && element.eliminado == true) {
                $scope.pagina.roles[index].eliminado = false;
                agregado = true;
                break;
            }
        }
        if (!agregado) {
            $scope.pagina.roles.push($scope.rolSelected);
        }
    }
    $scope.ocultar = function() {
        $scope.rolSelected = {};
        $scope.seleccionado = false;
    }
    $scope.eliminarRol = function(rol) {
        rol.eliminado = true;
    }
    $scope.guardar = function() {
        doRequest("POST", "/bonita/API/extension/AnahuacINVPRestAPI?url=updateBusinessAppMenu&p=0&c=999", {}, $scope.pagina, function(datos) {
            doRequest("GET", "/bonita/API/extension/AnahuacINVPRestGet?url=getBusinessAppMenu&p=0&c=999", {}, null, function(datos) {
                $scope.properties.content = datos;
                $scope.ocultar();
            })
        })
    }
}