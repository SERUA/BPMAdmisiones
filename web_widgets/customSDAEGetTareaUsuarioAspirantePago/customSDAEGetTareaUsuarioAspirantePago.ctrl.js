function ($scope, $http) {

    function getCurrentTaskId() {
        $http.get($scope.properties.url).success((data) => {
            if (data.length) {
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
                getCurrentContext();
            }
        }).error((err) => {
            console.log("Error al obtener las tareas asignadas al usuario" + JSON.stringify(err));
        });
    }

    function getCurrentContext() {
        $http.get($scope.properties.urlContext).success((data) => {
            if (data.solicitudApoyoEducativo_ref) {
                getModelSolicitudApoyoEducativo("../" + data.solicitudApoyoEducativo_ref.link);
                
                if(data.hermanos_ref){
                    getModelHermanos("../" + data.hermanos_ref.link);
                } 
                if(data.autos_ref){
                    getModelAutos("../" + data.autos_ref.link);
                }
                if(data.bienesRaices_ref){
                    getModelBienesRaices("../" + data.bienesRaices_ref.link); 
                }
                if(data.documentosSolicitante_ref){
                    getModelDocumentos("../" + data.documentosSolicitante_ref.link);
                }
                if(data.imagenesSocEcoSolicitante_ref){
                    getModelImagenesSocioEco("../" + data.imagenesSocEcoSolicitante_ref.link);
                }

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
            }
        }).error((err) => {
            console.log("Error al obtener el context");
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

    function getModelHermanos(url) {
        $scope.properties.hermanos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.hermanos = data;
            } else {
                $scope.properties.hermanos = [];
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
            } else {
                $scope.properties.autos = [];
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
            } else {
                $scope.properties.bienesRaices = [];
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

    $scope.$watch("properties.idCampus", () => {
        if($scope.properties.idCampus){
            getConfigPago();
        }
    });

    function getConfigPago(){
        let url = "../API/extension/AnahuacBecasRestGET?url=getConfiguracionPagoEstudioSocEcoAspirante&p=0&c=0&idCampus=" + $scope.properties.idCampus
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.configuracionesPago = data[0];
            }
        }).error((err) => {
            $scope.properties.configuracionesPago = {};
        });
    }

    $scope.$watch("properties.lstDocumentosByTipoApoyo", ()=>{
        if($scope.properties.lstDocumentosByTipoApoyo){
            for(let i = 0; i< $scope.properties.documentos.length; i++){
                $scope.properties.documentos[i].catManejoDocumentos = $scope.properties.lstDocumentosByTipoApoyo[i];
            }
        }
    });

    $scope.$watch("properties.lstImagenesByTipoApoyo", ()=>{
        if($scope.properties.lstImagenesByTipoApoyo){
            for(let i = 0; i< $scope.properties.imagenesSocioEco.length; i++){
                $scope.properties.imagenesSocioEco[i].imagenSocioEconomico = $scope.properties.lstImagenesByTipoApoyo[i];
            }
        }
    });
    
    $scope.getOrderInformation = function(){
        let url = "/bonita/API/extension/AnahuacRest?url=getOrderDetails&p=0&c=10";
        $http.post(url, $scope.properties.orderObject).success(function(success){
            $scope.properties.loadedOrder = true;
            $scope.properties.orderOutput = success.data[0];
            if(success.data[0].status === "paid"){
                $scope.properties.navigationVar = "orderPaid";    
            }
            
            $scope.properties.loadedOrder = true;
        }).error(function(error){
            $scope.properties.loadedOrder = true;
            swal("Error", JSOM.stringify(error), "error");
        }).finally(function(){
          
        })  
    };
    
    $scope.$watch("properties.orderObject", function(){
        if($scope.properties.orderObject){
            $scope.getOrderInformation();
        } 
    });
}