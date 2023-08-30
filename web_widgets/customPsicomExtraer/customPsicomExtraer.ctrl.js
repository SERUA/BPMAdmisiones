function ($scope, $http) {
    var vm = this;
    function getPsicom(){
        
        let url =  window.location.protocol + "//" + window.location.hostname + "/bonita/API/extension/AnahuacRestGet?url=getPsicometricoCompleto&p=0&c=10&caseId=" + $scope.properties.caseId+"&intentos="+$scope.properties.intentos;
        
        $http.get(url).success((success)=>{
            console.log(success);
            if(success.data.length > 0){
                $scope.properties.testPsicom = success.data[0];   
            }
        }).error((err)=>{
            alert("no se pudo obtener le psicom" +  JSON.stringify(err));
        });
    }
    
    $scope.$watch("properties.caseId", ()=>{
        if($scope.properties.caseId){
           getPsicom(); 
           if(sesionid == null){
            getSesion($scope.properties.caseId);    
           }
           
        }
    });
    
    function getSesion(value){
        vm.busy = true;
            var dataToSend={"caseId":value}
            var req = {
                method: "POST",
                url: "/bonita/API/extension/AnahuacRest?url=postGetIdSesionByCaseId&p=0&c=9999",
                data: angular.copy(dataToSend),
            };

            return $http(req)
            .success(function(data, status) {
                $scope.properties.sesionid=data.data[0].idsesion;
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {
                vm.busy = false;
            });
    }
    /*$scope.$watch('properties.idbanner', function(value) {
    if (angular.isDefined(value) && value !== null) {
         vm.busy = true;
    var dataToSend={"caseid":value}
    var req = {
      method: "POST",
      url: "/bonita/API/extension/AnahuacRest?url=postGetIdSesionByIdBanner&p=0&c=9999",
      data: angular.copy(dataToSend),
    };

    return $http(req)
      .success(function(data, status) {
        $scope.properties.sesionid=data.data[0].idsesion;
      })
      .error(function(data, status) {
       console.error(data)
      })
      .finally(function() {
        vm.busy = false;
      });
    }
  });*/
}