function PbTableCtrl($scope) {

    this.isArray = Array.isArray;
    
    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };
    
    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };
    
    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
    
    $scope.removeItem = function(_row){
        $scope.properties.content.splice($scope.properties.content.indexOf(_row), 1);
    }
    
    $scope.getSituacionAuto = function(_id){
        let output = "";
        for(let catSituacion of $scope.properties.lstSituacionAuto.data)  {
            if(catSituacion.persistenceId === _id){
                output = catSituacion.descripcion;
                break;
            }
        }
        
        return output;
    };
}
