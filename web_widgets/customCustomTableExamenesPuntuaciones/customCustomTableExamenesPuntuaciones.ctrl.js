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
      $scope.$watch('properties.objetoExamenes', function(value) {
          if (angular.isDefined(value) && value !== null) {
              if(value.invp!=""){
                 $scope.properties.puntuacionINVP=parseInt(value.invp) ? parseInt(value.invp) : "";
              }
          }
      });
      
     $scope.$watch('properties.puntuacionINVP', function(value) {
           if($scope.properties.puntuacionINVP != null &&$scope.properties.puntuacionINVP > 3){
               console.log("valor mayor a 3");
           }
      });
    $scope.getTotal=function(){
      try{
        return parseInt(($scope.properties.objetoExamenes.paan==""||$scope.properties.objetoExamenes.paan==null||$scope.properties.objetoExamenes.paan==undefined)?"0":$scope.properties.objetoExamenes.paan) + parseInt(($scope.properties.objetoExamenes.paav==""||$scope.properties.objetoExamenes.paav==null||$scope.properties.objetoExamenes.paav==undefined)?"0":$scope.properties.objetoExamenes.paav) + parseInt(($scope.properties.objetoExamenes.para==""||$scope.properties.objetoExamenes.para==null||$scope.properties.objetoExamenes.para==undefined)?"0":$scope.properties.objetoExamenes.para)
      }catch(e){
        return 0
      }
    }
    
    
    $scope.forceKeyPressUppercase = function(e){
      var charInput = e.keyCode;
      var limite = 1;
      if((charInput >=48) && (charInput <=51)&&(e.target.value.length) <limite){
  
      }else{
          var start = e.target.selectionStart;
          var end = e.target.selectionEnd;
          e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
          e.target.setSelectionRange(start+1, start+1);
           e.preventDefault();
      }
    }
    
    $scope.formatFecha = function(fecha){
        let newFecha = `${fecha.slice(0,4)}/${fecha.slice(5,7)}/${fecha.slice(8,10)}`;
        return newFecha;
    }    
}  