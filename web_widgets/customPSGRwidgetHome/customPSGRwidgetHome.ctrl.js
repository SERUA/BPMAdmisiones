
function ($scope, $http) {
    
    $scope.estatus = "";
    
    $scope.redirectTo = function(_url){
        window.top.location.href = window.location.protocol + "//" + window.location.host + "/apps/pg_aspirante/" + _url;
    }
    
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
                    $scope.estatus = angular.copy($scope.registro.estatus_solicitud);
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
    
    $scope.setEstatus = function(_paso){
        if(_paso === 1){
            return "finished";
        } else if(_paso === 2){
            if($scope.estatus === "solicitud_iniciada" || $scope.estatus === "aspirante_validado" || $scope.estatus === "modificaciones_solicitadas"){
                return "current";  
            } else if($scope.estatus === "solicitud_completada" || $scope.estatus === "solicitud_aprobada_admin" || $scope.estatus === "solicitud_rechazada_admin"){
                return "finished"
            } else {
                return "pending"
            }
        } else if(_paso === 3){
            if($scope.estatus === "solicitud_iniciada" || $scope.estatus === "aspirante_validado" || $scope.estatus === "modificaciones_solicitadas"){
                return "current";  
            } else if($scope.estatus === "solicitud_completada" || $scope.estatus === "solicitud_aprobada_admin" || $scope.estatus === "solicitud_rechazada_admin"){
                return "finished"
            } else {
                return "pending"
            }
        } else {
            return "pending"
        }
    }
}