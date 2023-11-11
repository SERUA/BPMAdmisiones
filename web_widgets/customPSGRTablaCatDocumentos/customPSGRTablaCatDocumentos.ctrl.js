function PbTableCtrl($scope, $http) {

    this.isArray = Array.isArray;

    //this.isClickable = function () {
    //    return $scope.properties.isBound('selectedRow');
    //};
    
    var ctrl = this;

    ctrl.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRowEdit = function (event, row) {
        debugger;
        
        var propiedadesActuales = Object.keys($scope.properties.dataToFilter);

            if (this.isClickable()) {
                $scope.properties.selectedRow = row;
                $scope.properties.navigationVar = "editar";
            }
        
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    $scope.$watch("properties.urlGet", function(){
        if($scope.properties.urlGet){
            $scope.getCatalogo();
        }
    });
    
    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload === true){
            $scope.properties.reload = false;
            $scope.getCatalogo();
        }
    });
    
    $scope.$watchGroup(['properties.dataToFilter.campus_pid.persistenceId', 'properties.dataToFilter.posgrado_pid.persistenceId'], function(newValues, oldValues) {
        debugger;
        if (newValues[0] !== oldValues[0] || newValues[1] !== oldValues[1]) {
            $scope.getCatalogo();
        }
    });

    $scope.getCatalogo = function () {
        debugger;
        if ($scope.properties.dataToFilter.campus_pid !== null && $scope.properties.dataToFilter.campus_pid !== undefined &&
            $scope.properties.dataToFilter.posgrado_pid !== null && $scope.properties.dataToFilter.posgrado_pid !== undefined) {
        
            $http.post($scope.properties.urlGet, $scope.properties.dataToFilter, {})
            .then(function(response) {
                if (response.data && response.data.data) {
                    $scope.properties.content = response.data.data;
                    console.log(response.data.data);
                } else {
                    swal("¡Respuesta inesperada!", "La respuesta no tiene la propiedad 'data' esperada.", "error");
                }
            })
            .catch(function(error) {
                swal("¡Algo ha fallado!", error.data ? error.data.error : "Error desconocido", "error");
            });
        } else {
            $scope.properties.content = [];
        }
    };

    this.selectRowDelete = function(row) {
        
        swal("¿Esta seguro que desea eliminar?", {
            icon: "warning",
            buttons: {
                cancel: "No", 
                catch: {
                    text: "Si",
                    value: true,
                }
            },
        }).then((value) => {
            if(value){
                row.isDeleted = true;
                
                this.handleTrashClick(row);
            }
        }); 
    };

    this.handleTrashClick = function (row) {
        debugger;
        var persistenceid = row.persistenceId;
        $scope.deleteCatalogo({ persistenceid: persistenceid })
            .then(function () {
                var index = $scope.properties.content.indexOf(row);
                if (index !== -1) {
                    $scope.properties.content.splice(index, 1);
                }
            });
    };

    $scope.deleteCatalogo = function (dataToDelete) {
        $scope.busy = true;
    
        return $http({
            method: 'POST',
            url: $scope.properties.urlDelete,
            data: dataToDelete,
            headers: { 'Content-Type': 'application/json;charset=utf-8' }
        }).then(function (response) {
            swal("OK", "Registro eliminado correctamente", "success");
        }).catch(function (error) {
            swal("¡Algo ha fallado!", error.data.error, "error");
        }).finally(function () {
            $scope.busy = false;
        });
    };
}