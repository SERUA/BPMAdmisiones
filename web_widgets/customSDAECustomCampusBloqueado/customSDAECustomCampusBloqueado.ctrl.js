function PbButtonCtrl($scope, $http) {
    $scope.$watch("properties.objSolicitudDeAdmision", function(){
        if($scope.properties.objSolicitudDeAdmision){
            getCampusBloqueado();
        }
    });
    
    function validarPeriodoVencido(_solicitud){
        var fecha = new Date(_solicitud.catPeriodo.fechaFin.slice(0, 10));
        if(fecha < new Date()){
            // swal("¡Periodo vencido!", "Tu solicitud de admisión está registrada para un periodo vencido. Es necesario que te comuniques con el área de admisiones del campus para actualizarlo y puedas continuar con tu solicitud de apoyo educativo.", "warning");
            
            var content = document.createElement('div');
            let message  = "<p style='text-align: justify;'>Tu solicitud de admisión está registrada para un periodo vencido. "
                + "<br> Es necesario que te comuniques con el área de admisiones del campus para actualizarlo y puedas continuar con tu solicitud de apoyo educativo.</p>"
            content.innerHTML = message;
        
            swal({
                title: "¡Periodo vencido!",
                content: content,
                icon: "warning"
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
    
    function getCampusBloqueado(){
        let campus = angular.copy($scope.properties.objSolicitudDeAdmision.catCampusEstudio.grupoBonita);
        if(campus === "CAMPUS-MNORTE" || campus === "CAMPUS-MSUR" || campus === "CAMPUS-MAYAB" || campus === "CAMPUS-CORDOBA" || campus === "CAMPUS-TAMPICO" || campus === "CAMPUS-XALAPA" || campus === "CAMPUS-PUEBLA"){
            swal({
                title: "¡Atención!",
                text: "Para conocer el proceso de solicitud de apoyo educativo, contacta al área de admisiones del campus.",
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