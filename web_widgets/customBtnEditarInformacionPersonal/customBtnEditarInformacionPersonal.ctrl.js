function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {
var vm = this;

  this.action = function action() {
      debugger
    if($scope.properties.banderaEdicion == true){
        $scope.properties.banderaEdicion = false;
        $scope.properties.banderaOcultarBotones = false;
        $scope.properties.ocultarBtnEditarInformacionPersonal = true;
        
    }else if($scope.properties.banderaEdicion == false){
        $scope.properties.banderaEdicion = true
        
    }
      
  };

}

