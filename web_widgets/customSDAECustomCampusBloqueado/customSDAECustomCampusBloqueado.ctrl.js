function PbButtonCtrl($scope, $http) {
    $scope.$watch("properties.objSolicitudDeAdmision", function(){
        if($scope.properties.objSolicitudDeAdmision){
            getCampusBloqueado();
        }
    });
    
    function validarPeriodoVencido(_solicitud){
        debugger;
        swal({
            title: "¡Atención!",
            text: "Tu solicitud de admisión está registrada para un periodo vencido. Es necesario que te comuniques con el área de admisiones del campus para actualizarlo y puedas continuar con tu solicitud de apoyo educativo.",
            icon: "info"
        })
    }
    
    function getCampusBloqueado(){
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
        } else {
            validarPeriodoVencido(angular.copy($scope.properties.objSolicitudDeAdmision));
        }
    }
}
