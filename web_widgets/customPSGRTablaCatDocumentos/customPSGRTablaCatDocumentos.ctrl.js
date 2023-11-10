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

    $scope.getCatalogo = function () {
        debugger;
        $http.post($scope.properties.urlGet, $scope.properties.dataToFilter).succes(function (response) {
                $scope.properties.content = response.data;
            })
            .catch(function (error) {
                swal("¡Algo ha fallado!", error.data.error, "error");
            });
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
        var persistenceid = row.persistenceId; // Obtener el persistenceid del row
        $scope.deleteCatalogo({ persistenceid: persistenceid }) // Enviar persistenceid como objeto JSON
            .then(function () {
                // Actualizar la matriz properties.content después de eliminar el registro
                var index = $scope.properties.content.indexOf(row);
                if (index !== -1) {
                    $scope.properties.content.splice(index, 1);
                }
            });
    };

    $scope.deleteCatalogo = function (dataToDelete) {
        // Realiza la solicitud HTTP para eliminar el registro utilizando la matriz JSON
        $scope.busy = true;
    
        return $http({
            method: 'POST',
            url: $scope.properties.urlDelete,
            data: dataToDelete,
            headers: { 'Content-Type': 'application/json;charset=utf-8' }
        }).then(function (response) {
            // Procesa la respuesta de eliminación si es necesario
            swal("OK", "Registro eliminado correctamente", "success");
            // Actualiza la vista o realiza otras acciones necesarias después de la eliminación
        }).catch(function (error) {
            swal("¡Algo ha fallado!", error.data.error, "error");
        }).finally(function () {
            $scope.busy = false;
        });
    };
}