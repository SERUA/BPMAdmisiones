function PbTableCtrl($scope, $http) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.handlePencilClick = function (row) {
    if (this.isClickable()) {
        $scope.properties.selectedRow = row;
        $scope.properties.navigationVar = "editar";
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
  $scope.$watch("properties.urlGet", function(){
      debugger;
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
    
    $scope.getCatalogo = function (){
        $http.post($scope.properties.urlGet, {}).success(function(_response){
            $scope.properties.content = _response;
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        });
    }
    
    this.handleTrashClick = function (row) {
        var persistenceid = row.persistenceid; // Obtener el persistenceid del row
        $scope.deleteCatalogo({ persistenceid: persistenceid }); // Enviar persistenceid como objeto JSON
    };

    $scope.deleteCatalogo = function (dataToDelete) {
        debugger;
        // Realiza la solicitud HTTP para eliminar el registro utilizando la matriz JSON
        $scope.busy = true;
    
        $http({
            method: 'POST',
            url: $scope.properties.urlDelete,
            data: dataToDelete,
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        }).then(function(response) {
            // Procesa la respuesta de eliminación si es necesario
            swal("OK", "Registro eliminado correctamente", "success");
            // Actualiza la vista o realiza otras acciones necesarias después de la eliminación
        }).catch(function(error) {
            swal("¡Algo ha fallado!", error.data.error, "error");
        }).finally(function(){
            $scope.busy = false;
        });
    };

}