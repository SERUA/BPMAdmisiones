function ($scope, $http) {
    
    function doRequest(){
        let url = "../API/extension/AnahuacBecasRestGET?url=getConfiguracionCampus&p=0&c=10&idCampus=" + $scope.properties.idCampus
        $http.get(url).success((success)=>{
            if(success.data[0]){
                $scope.properties.objConfiguracionSDAE = success.data[0];
            } else {
                $scope.properties.objConfiguracionSDAE = {
                    "urlReglamento": "",
                    "tieneFinanciamiento":false,
                    "descuentoProntoPago":false,
                    "interesInscripcion":false,
                    "interesColegiatura":false,
                    "porcentajeInteresFinanciamiento": 0,
                    "idCampus": $scope.properties.idCampus,
                    "promedioMinimo": 0
                }
            }
        }).error((err)=>{
             $scope.properties.objConfiguracionSDAE = {
                    "urlReglamento": "",
                    "tieneFinanciamiento":false,
                    "descuentoProntoPago":false,
                    "interesInscripcion":false,
                    "interesColegiatura":false,
                    "porcentajeInteresFinanciamiento": 0,
                    "idCampus": $scope.properties.idCampus,
                    "promedioMinimo": 0
                }
            console.log(error);
        })
    }
    
    $scope.$watch("properties.idCampus", ()=>{
       if($scope.properties.idCampus){
            doRequest();
       } 
    });
}