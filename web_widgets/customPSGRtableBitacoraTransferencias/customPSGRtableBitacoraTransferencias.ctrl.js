function PbTableCtrl($scope, $http) {

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
    
    $scope.dataToSend = {
        limit: 20,
        offset: 0
    }
    
    function selectBitacoraTransferencias(){
        let url = "../API/extension/posgradosRest?url=selectBitacoraTransferencias";
        debugger;
        $http.post(url, $scope.dataToSend).success(function(succsess){
            debugger;
            $scope.properties.content = succsess.data;
        }).error(function(err){
            
        })
    }
    
    selectBitacoraTransferencias();
}
