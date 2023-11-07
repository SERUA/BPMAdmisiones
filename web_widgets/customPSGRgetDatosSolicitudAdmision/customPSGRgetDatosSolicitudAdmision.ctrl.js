function ($scope, $http) {
    
    /* $http.get("../API/system/session/1").success(function(_data){
        let url = "../API/bpm/humanTask?p=0&c=10&f=assigned_id=" + _data.user_id;
       
        $http.get(url).success(function(_data2){
            $scope.properties.taskId = _data2[0].id;
        }).error(function(){
            console-log("error al obtener las tareas");
        });
    }); */
    
    var url = "../API/bpm/humanTask?c=1&f=state=ready&p=0&f=caseId=" + $scope.properties.caseId;
    var urlContext = "";
    var task = false;
    function getCurrentTaskId() {
        $http.get(url).success((data) => {
            if (data.length) {
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
                //getCurrentContext();
            }
        }).error((err) => {
            debugger;
            task = false;
            //getModelSolicitudApoyoEducativoAlt("../API/bdm/businessData/com.anahuac.SDAE.model.SolicitudApoyoEducativo?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            //getModelAutos("../API/bdm/businessData/com.anahuac.SDAE.model.AutosSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            //getModelHermanos("../API/bdm/businessData/com.anahuac.SDAE.model.HermanosSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            //getModelBienesRaices("../API/bdm/businessData/com.anahuac.SDAE.model.BienesRaicesSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            //getModelDocumentosNoTask("../API/extension/AnahuacBecasRestGET?url=getDocumentosByCaseId&c=0&p=0&caseId=" + $scope.properties.caseId);
            //getModelImagenesSocioEcoNoTask("../API/extension/AnahuacBecasRestGET?url=getImagenesByCaseId&c=0&p=0&caseId=" + $scope.properties.caseId);
        });
    }
    
    function getCurrentContext() {
        $http.get($scope.properties.urlContext).success((data) => {
            task = true;
            if (data.solicitudApoyoEducativo_ref) {
                getModelSolicitudApoyoEducativo("../" + data.solicitudApoyoEducativo_ref.link);
                getModelHermanos("../" + data.hermanos_ref.link);
                getModelAutos("../" + data.autos_ref.link);
                getModelBienesRaices("../" + data.bienesRaices_ref.link);
                getModelDocumentos("../" + data.documentosSolicitante_ref.link);
                getModelImagenesSocioEco("../" + data.imagenesSocEcoSolicitante_ref.link);
                
                $scope.properties.lstDocumentos = [];
                $scope.properties.autos = [];
                let lstDoc = [];
                let newValue = {
                    "filename": null,
                    "tempPath": null,
                    "contentType": null,
                    "id": null
                };

                if (data.lstDocumentos_ref) {
                    for (let documento of data.lstDocumentos_ref) {
                        newValue.id = documento["id"] + "";
                        lstDoc.push(angular.copy(newValue));
                    }

                    $scope.properties.lstDocumentos = lstDoc;
                }
            } else {
                debugger;
                task = false;
                getModelSolicitudApoyoEducativoAlt("../API/bdm/businessData/com.anahuac.SDAE.model.SolicitudApoyoEducativo?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
                getModelAutos("../API/bdm/businessData/com.anahuac.SDAE.model.AutosSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
                getModelHermanos("../API/bdm/businessData/com.anahuac.SDAE.model.HermanosSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
                getModelBienesRaices("../API/bdm/businessData/com.anahuac.SDAE.model.BienesRaicesSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
                getModelDocumentosNoTask("../API/extension/AnahuacBecasRestGET?url=getDocumentosByCaseId&c=0&p=0&caseId=" + $scope.properties.caseId);
                getModelImagenesSocioEcoNoTask("../API/extension/AnahuacBecasRestGET?url=getImagenesByCaseId&c=0&p=0&caseId=" + $scope.properties.caseId);
            }
        }).error((err) => {
            debugger;
            task = false;
            getModelSolicitudApoyoEducativoAlt("../API/bdm/businessData/com.anahuac.SDAE.model.SolicitudApoyoEducativo?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            getModelAutos("../API/bdm/businessData/com.anahuac.SDAE.model.AutosSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            getModelHermanos("../API/bdm/businessData/com.anahuac.SDAE.model.HermanosSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            getModelBienesRaices("../API/bdm/businessData/com.anahuac.SDAE.model.BienesRaicesSolicitante?q=findByCaseId&p=0&c=10&f=caseId=" + $scope.properties.caseId);
            getModelDocumentosNoTask("../API/extension/AnahuacBecasRestGET?url=getDocumentosByCaseId&c=0&p=0&caseId=" + $scope.properties.caseId);
            getModelImagenesSocioEcoNoTask("../API/extension/AnahuacBecasRestGET?url=getImagenesByCaseId&c=0&p=0&caseId=" + $scope.properties.caseId);
        });
    }
    
    getCurrentTaskId();
    
}