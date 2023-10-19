
function ($scope, $http) {
    
    getCasosIniciados();
    
    function getCasosIniciados(){
        
        $http.get("../API/system/session/1")
        .then(function(response) {
            $scope._userid = response.data.user_id;
            
            $http.get("../API/bpm/case?p=0&c=100&f=started_by=" + $scope._userid)
            .then(function(response) {
                $scope.caseid = response.data[0].rootCaseId;
                $http.get("../API/bdm/businessData/com.anahuac.posgrados.model.PSGRRegistro?q=findByCaseid&p=0&c=999&f=caseid=" + $scope.caseid)
                .then(function(response) {
                    $scope.registro = response.data[0];
                    let estatus = angular.copy($scope.registro.estatus_solicitud);
                    if(estatus === "solicitud_iniciada" || estatus === "solicitud_iniciada"){
                        // document.querySelector(".circle-2").addClass("current")
                    }
                })
                .catch(function(error) {
                    console.error('Error al obtener casos iniciados:', error);
                });
            })
            .catch(function(error) {
                console.error('Error al obtener casos iniciados:', error);
            });
        })
        .catch(function(error) {
            console.error('Error al obtener casos iniciados:', error);
        });
       
    }
}