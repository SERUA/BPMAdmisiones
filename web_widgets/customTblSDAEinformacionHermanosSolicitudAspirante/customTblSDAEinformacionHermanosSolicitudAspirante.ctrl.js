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

    $scope.disableEscolar = function(_row, _index){
        if(!_row.isEstudia){
            $scope.properties.content[_index].institucion = "";
            $scope.properties.content[_index].isTieneBeca = false;
            $scope.properties.content[_index].porcentajeBecaAsignado = 0;
            $scope.properties.content[_index].colegiaturaMensual = 0;
        }
    }

    $scope.disableBeca = function(_row, _index){
        if(!_row.isTieneBeca){
            $scope.properties.content[_index].porcentajeBecaAsignado = 0;
        }
    }

    $scope.disableTrabaja = function(_row, _index){
        if(!_row.isTrabaja){
            $scope.properties.content[_index].empresa = "";
            $scope.properties.content[_index].ingresoMensual = 0;
        }
    }
    
    $scope.forceKeyPressUppercase = function (e, _type, _maxlength, _max) {
        var charInput = e.keyCode;
        var content = e.key;
        var limite = _maxlength === 1 ? 50 : _maxlength;
        let valid = true;
        
        if(_type === "number"){
            if((e.keyCode > 47 && e.keyCode < 59)){
                valid = true;
            } else{
                valid = false;
            }
        } else {
            valid = true;
        }
        
        if(_type === "number"){
            let fullNumber = parseInt(e.target.value + content);
            if ((e.target.value.length) >= limite || !valid || (fullNumber > _max)) {
                e.preventDefault();  
            } 
        } else {
            if ((e.target.value.length) >= limite || !valid) {
                let start = e.target.selectionStart;
                let end = e.target.selectionEnd;
                e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
                e.target.setSelectionRange(start + 1, start + 1);
                e.preventDefault();  
            } 
        }
    }
}