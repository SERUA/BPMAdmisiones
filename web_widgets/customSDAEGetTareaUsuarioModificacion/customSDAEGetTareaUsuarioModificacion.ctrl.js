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
                $scope.properties.solicitudApoyoEducativo.modificarDocumentos = false;
                
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

    $scope.$watch("properties.lstDocumentosByTipoApoyo", ()=>{
        if($scope.properties.modificarDocumentos === true){
            $scope.properties.solicitudApoyoEducativo.urlVideoYouTube = "";
            $scope.properties.solicitudApoyoEducativo.urlCurriculum = ""
            $scope.properties.documentos = []
            for(let documentObject of $scope.properties.lstDocumentosByTipoApoyo){
                let templateObject = {
                    "persistenceId_string":"",
                    "catManejoDocumentos": documentObject,
                    "urlDocumento":"",
                    "caseId": $scope.properties.caseId,
                    "catManejoDocumentos_id": documentObject.persistenceId
                };

                $scope.properties.documentos.push(templateObject);
            }

            $scope.properties.imagenesSocioEco = [];
            for(let imageObject of $scope.properties.lstImagenesByTipoApoyo){
                let templateObject = {
                    "persistenceId_string":"",
                    "imagenSocioEconomico": imageObject,
                    "urlImagen":"",
                    "caseId": $scope.properties.caseId,
                    "imagenSocioEconomico_id": imageObject.persistenceId
                };

                $scope.properties.imagenesSocioEco.push(templateObject);
            }



        } else if($scope.properties.lstDocumentosByTipoApoyo && $scope.properties.documentos){
			let newDocuments = [];
			if($scope.properties.documentos.length < $scope.properties.lstDocumentosByTipoApoyo.length){
			    
				for(let documentObject of $scope.properties.lstDocumentosByTipoApoyo){
					let templateObject = {
						"persistenceId_string":"",
						"catManejoDocumentos": documentObject,
						"urlDocumento":"",
						"caseId": $scope.properties.caseId,
						"catManejoDocumentos_id": documentObject.persistenceId
					};

					let documentFound = false;
					
					for(let processDocument of $scope.properties.documentos){
						if(processDocument.catManejoDocumentos_id === documentObject.persistenceId){
							documentFound = true; 
							processDocument.catManejoDocumentos = documentObject;
							break;
						}	
					}

					if(!documentFound){
						newDocuments.push(templateObject);
					} 

				}

				for(let documentObject of newDocuments){
					$scope.properties.documentos.push(documentObject);
				}

			}  else if($scope.properties.documentos.length > $scope.properties.lstDocumentosByTipoApoyo.length){
				for(let i = 0; i < $scope.properties.documentos.length; i++){
					let documentFound = false;
					for(let documentObject of $scope.properties.lstDocumentosByTipoApoyo){
						if(documentObject.persistenceId === $scope.properties.documentos[i].catManejoDocumentos_id){
							documentFound = true;
							$scope.properties.documentos[i].catManejoDocumentos = documentObject;
							break;
						}
					}

					if(!documentFound){
						$scope.properties.documentos.splice(i,1);
						i--;
					} 
				}
			} else {
				for(let i = 0; i< $scope.properties.documentos.length; i++){
					$scope.properties.documentos[i].catManejoDocumentos = $scope.properties.lstDocumentosByTipoApoyo[i];
				}
			}
        }
    });

    $scope.$watch("properties.lstImagenesByTipoApoyo", ()=>{
        if($scope.properties.lstImagenesByTipoApoyo && $scope.properties.imagenesSocioEco){
			let newImages = [];
			if($scope.properties.imagenesSocioEco.length < $scope.properties.lstImagenesByTipoApoyo.length){
				for(let imageObject of $scope.properties.lstImagenesByTipoApoyo){
					let templateObject = {
						"persistenceId_string":"",
						"imagenSocioEconomico": imageObject,
						"urlImagen":"",
						"caseId": $scope.properties.caseId,
						"imagenSocioEconomico_id": imageObject.persistenceId
					};

					let imageFound = false;
					
					for(let processImg of $scope.properties.imagenesSocioEco){
						if(processImg.imagenSocioEconomico_id === imageObject.persistenceId){
							imageFound = true; 
							processImg.imagenSocioEconomico = imageObject;
							break;
						}	
					}

					if(!imageFound){
						newImages.push(templateObject);
					} 

				}

				for(let imageObject of newImages){
					$scope.properties.imagenesSocioEco.push(imageObject);
				}

			}  else if($scope.properties.imagenesSocioEco.length > $scope.properties.lstImagenesByTipoApoyo.length){
				for(let i = 0; i < $scope.properties.imagenesSocioEco.length; i++){
					let imageFound = false;
					for(let imageObject of $scope.properties.lstImagenesByTipoApoyo){
						if(imageObject.persistenceId === $scope.properties.imagenesSocioEco[i].imagenSocioEconomico_id){
							imageFound = true;
							$scope.properties.imagenesSocioEco[i].imagenSocioEconomico = imageObject;
							break;
						}
					}

					if(!imageFound){
						$scope.properties.imagenesSocioEco.splice(i,1);
						i--;
					} 
				}
			} else {
				for(let i = 0; i< $scope.properties.imagenesSocioEco.length; i++){
					$scope.properties.imagenesSocioEco[i].imagenSocioEconomico = $scope.properties.lstImagenesByTipoApoyo[i];
				}
			}
        }
    });
}