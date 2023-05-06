function PbButtonCtrl($scope, $http) {
    $scope.$watch("properties.objSolicitudDeAdmision", function(){
        if($scope.properties.objSolicitudDeAdmision){
            let campus = angular.copy($scope.properties.objSolicitudDeAdmision.catCampusEstudio.grupoBonita);
            if(campus === "CAMPUS-MNORTE" || campus === "CAMPUS-MSUR" || campus === "CAMPUS-MAYAB"){
                swal({
                    title: "¡Atención!",
                    text: "Para conocer el proceso de solicitud de apoyo educativo, contacta al área de becas del campus.",
                    icon: "info"
                }).then(() => {
                    let url = "	/bonita/logoutservice?redirect=false";
                    var req = {
                        method: "POST",
                        url: url,
                    };
              
                    return $http(req).success(function(data){
                        let href = window.location.protocol + "//" + window.location.host + $scope.properties.urlToRedirect;
                        window.top.location = href;
                    });
                });
            }
        }
    });
}
