function ($scope, $http) {

    var task = false;
    function getCurrentTaskId() {
        $http.get($scope.properties.url).success((data) => {
            if (data.length) {
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
                getCurrentContext();
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

    function getCurrentContext() {
        debugger;
        $http.get($scope.properties.urlContext).success((data) => {
            debugger;
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

    function addDataToDocuments(_document) {

        let newValue = {
            "filename": _document["fileName"],
            "tempPath": null,
            "contentType": _document["contentMimeType"],
            "id": _document["id"] + ""
        };

        return newValue;
    }

    function getModelSolicitudApoyoEducativo(url) {
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = data;
                $scope.properties.solicitudApoyoEducativoInput.modificarDocumentos = true;
                
                if($scope.properties.isSolicitud){
                    if(data.pageIndex > 1){
                        $scope.properties.selectedIndex = data.pageIndex; 
                    }
                }
                let links = $scope.properties.solicitudApoyoEducativo.links;

                for (let link of links) {
                    getLazyRefModel(".." + link.href, link.rel);
                }
            }
        }).error((err) => {
            swal("Error", "Error al obtener el model. " + err, "error");
        });
    }

    function getModelSolicitudApoyoEducativoAlt(url) {
        debugger;
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = data[0];
                $scope.properties.solicitudApoyoEducativoInput.modificarDocumentos = true;
                
                // if($scope.properties.isSolicitud){
                //     if(data.pageIndex > 1){
                //         $scope.properties.selectedIndex = data.pageIndex; 
                //     }
                // }
                // let links = $scope.properties.solicitudApoyoEducativo.links;

                // for (let link of links) {
                //     getLazyRefModel(".." + link.href, link.rel);
                // }
            }
        }).error((err) => {
            swal("Error", "Error al obtener el model. " + err, "error");
        });
    }

    function getModelHermanos(url) {
        $scope.properties.hermanos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.hermanos = data;
            }
        }).error((err) => {
            $scope.properties.hermanos = [];
            console.log("hermanos vacío");
        });
    }

    function getModelAutos(url) {
        $scope.properties.autos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.autos = data;
            }
        }).error((err) => {
            $scope.properties.autos = [];
            console.log("autos vacío");
        });
    }

    function getModelBienesRaices(url) {
        $scope.properties.bienesRaices = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.bienesRaices = data;
            }
        }).error((err) => {
            $scope.properties.bienesRaices = [];
            console.log("bienesRaices vacío");
        });
    }

    function getModelDocumentos(url) {
        $scope.properties.documentos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.documentos = data;
                for(let i = 0; i< $scope.properties.documentos.length; i++){
                    $scope.properties.documentos[i].catManejoDocumentos = $scope.properties.lstDocumentosByTipoApoyo ? $scope.properties.lstDocumentosByTipoApoyo[i] : null;
                }
            }
        }).error((err) => {
            $scope.properties.documentos = buildDocumentList();
        });
    }

    function getModelDocumentosNoTask(url) {
        $scope.properties.documentos = [];
        $http.get(url).success((data) => {
            debugger;
            if (data) {
                $scope.properties.documentos = data.data;
            }
        }).error((err) => {
            $scope.properties.documentos = buildDocumentList();
        });
    }

    function buildDocumentList(){
        let list = [];
        for(let documentObject of $scope.properties.lstDocumentosByTipoApoyo){
            let templateObject = {
                "persistenceId_string":"",
                "catManejoDocumentos": documentObject,
                "urlDocumento":"",
                "caseId": $scope.properties.caseId,
                "catManejoDocumentos_id": documentObject.persistenceId
            };

            list.push(templateObject);

        }
        
        return list;
    }

    function getModelImagenesSocioEco(url) {
        $scope.properties.imagenesSocioEco = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.imagenesSocioEco = data;
                for(let i = 0; i< $scope.properties.imagenesSocioEco.length; i++){
                    $scope.properties.imagenesSocioEco[i].imagenSocioEconomico = $scope.properties.lstImagenesByTipoApoyo ? $scope.properties.lstImagenesByTipoApoyo[i] : null;
                }
            }
        }).error((err) => {
            $scope.properties.imagenesSocioEco = buildImagesList();
        });
    }

    function getModelImagenesSocioEcoNoTask(url) {
        $scope.properties.imagenesSocioEco = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.imagenesSocioEco = data.data;
            }
        }).error((err) => {
            $scope.properties.imagenesSocioEco = buildImagesList();
        });
    }

    function buildImagesList(){
        let list = [];
        for(let imageObject of $scope.properties.lstImagenesByTipoApoyo){
            let templateObject = {
                "persistenceId_string":"",
                "imagenSocioEconomico": imageObject,
                "urlImagen":"",
                "caseId": $scope.properties.caseId,
                "imagenSocioEconomico_id": imageObject.persistenceId
            };

            list.push(templateObject);

        }

        return list;
   }

    function getLazyRefModel(_url, _bdmFieldName) {
        $http.get(_url).success((data) => {
            $scope.properties.solicitudApoyoEducativo[_bdmFieldName] = data;
        }).error((err) => {
            console.log(_bdmFieldName + " vacío");
        });
    }

    $scope.$watch("properties.taskId", (oldValue, newValue) => {
        if ($scope.properties.taskId && oldValue !== newValue) {
            getCurrentContext();
        }
    });

    $scope.$watch("properties.reloadTask", () => {
        if ($scope.properties.reloadTask) {
            getCurrentTaskId();
        }
    });

    $scope.$watch("properties.url", () => {
        getCurrentTaskId();
    });

    $scope.$watch("properties.lstDocumentosByTipoApoyo", ()=>{
        if($scope.properties.lstDocumentosByTipoApoyo && task){
            for(let i = 0; i< $scope.properties.documentos.length; i++){
                $scope.properties.documentos[i].catManejoDocumentos = $scope.properties.lstDocumentosByTipoApoyo[i];
            }
        }
    });

    $scope.$watch("properties.lstImagenesByTipoApoyo", ()=>{
        if($scope.properties.lstImagenesByTipoApoyo && task){
            for(let i = 0; i< $scope.properties.imagenesSocioEco.length; i++){
                $scope.properties.imagenesSocioEco[i].imagenSocioEconomico = $scope.properties.lstImagenesByTipoApoyo[i];
            }
        }
    });
}