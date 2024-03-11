function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {

        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Submit task') {
            if($scope.properties.dataToChange == "agregar"){
                accionCatalogoInsert($scope.properties.urlInsert)
            }else if($scope.properties.dataToChange == "editar"){
                $scope.properties.urlUpdate = $scope.properties.urlUpdate;
                accionCatalogo($scope.properties.urlUpdate) 
            }
        } else if ($scope.properties.action === 'Start process') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

    function accionCatalogoInsert(urlInsert){
        vm.busy = true;
        
        if ($scope.properties.dataToSend.lstCatCampusInput[0].estado !== null &&
            $scope.properties.dataToSend.lstCatCampusInput[0].estado !== undefined) {

                var estado_pid;
                var estadoDescripcion = $scope.properties.dataToSend.lstCatCampusInput[0].estado.descripcion;

                var estadoEncontrado = $scope.properties.lstEstados.find(function(estado) {
                    return estado.descripcion === estadoDescripcion;
                });

                if (estadoEncontrado) {
                    estado_pid = estadoEncontrado.persistenceId;
                } else {
                    console.error("Estado no encontrado en lstEstados");
                }

                $scope.properties.dataToSend.lstCatCampusInput[0].estado_pid = estado_pid;
        }


        if ($scope.properties.pais.lstCatCampusInput[0].descripcion !== null &&
            $scope.properties.pais.lstCatCampusInput[0].descripcion !== undefined) {
                var pais_pid;
                var paisDescripcion = $scope.properties.pais.lstCatCampusInput[0].descripcion;

                var paisEncontrado = $scope.properties.lstPais.data.find(function(pais) {
                    return pais.descripcion === paisDescripcion;
                });

                if (paisEncontrado) {
                    pais_pid = paisEncontrado.persistenceId;
                } else {
                    console.error("Estado no encontrado en lstEstados");
                }
                $scope.properties.dataToSend.lstCatCampusInput[0].pais_pid = pais_pid;
        }

        $http.post(urlInsert, $scope.properties.dataToSend).success(function(_response){
            swal("OK", "Guardado correctamente", "success");
            $scope.properties.dataToChange = "tabla";
            $scope.properties.doRequest(urlPost);
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        }).finally(function(){
            vm.busy = false;
        });

    }

    function accionCatalogo(urlUpdate){
        vm.busy = true;

        if ($scope.properties.dataToSend.lstCatCampusInput[0].estado !== null &&
            $scope.properties.dataToSend.lstCatCampusInput[0].estado !== undefined) {
            
                var estado_pid;
                var estadoDescripcion = $scope.properties.dataToSend.lstCatCampusInput[0].estado.descripcion;

                var estadoEncontrado = $scope.properties.lstEstados.find(function(estado) {
                    return estado.descripcion === estadoDescripcion;
                });

                if (estadoEncontrado) {
                    estado_pid = estadoEncontrado.persistenceId;
                } else {
                    console.error("Estado no encontrado en lstEstados");
                }

                $scope.properties.dataToSend.lstCatCampusInput[0].estado_pid = estado_pid;
        }
        
        if ($scope.properties.pais.lstCatCampusInput[0].descripcion !== null &&
            $scope.properties.pais.lstCatCampusInput[0].descripcion !== undefined) {
                
                var pais_pid;
                var paisDescripcion = $scope.properties.pais.lstCatCampusInput[0].descripcion;

                var paisEncontrado = $scope.properties.lstPais.data.find(function(pais) {
                    return pais.descripcion === paisDescripcion;
                });

                if (paisEncontrado) {
                    pais_pid = paisEncontrado.persistenceId;
                } else {
                    console.error("Estado no encontrado en lstEstados");
                }
                $scope.properties.dataToSend.lstCatCampusInput[0].pais_pid = pais_pid;
        }

        

        $http.post(urlUpdate, $scope.properties.dataToSend).success(function(_response){
            swal("OK", "Guardado correctamente", "success");
            $scope.properties.dataToChange = "tabla"
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        }).finally(function(){
            vm.busy = false;
        });
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    function removeFromCollection() {
        if ($scope.properties.collectionToModify) {
            if (!Array.isArray($scope.properties.collectionToModify)) {
                throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
            }
            var index = -1;
            if ($scope.properties.collectionPosition === 'First') {
                index = 0;
            } else if ($scope.properties.collectionPosition === 'Last') {
                index = $scope.properties.collectionToModify.length - 1;
            } else if ($scope.properties.collectionPosition === 'Item') {
                index = $scope.properties.collectionToModify.indexOf($scope.properties.removeItem);
            }

            // Only remove element for valid index
            if (index !== -1) {
                $scope.properties.collectionToModify.splice(index, 1);
            }
        }
    }

    function addToCollection() {
        if (!$scope.properties.collectionToModify) {
            $scope.properties.collectionToModify = [];
        }
        if (!Array.isArray($scope.properties.collectionToModify)) {
            throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
        }
        var item = angular.copy($scope.properties.valueToAdd);

        if ($scope.properties.collectionPosition === 'First') {
            $scope.properties.collectionToModify.unshift(item);
        } else {
            $scope.properties.collectionToModify.push(item);
        }
    }

    function startProcess() {
	if($scope.properties.dataToChange2.id != undefined){
		if( $scope.properties.dataToChange2.id &&  $scope.properties.dataToChange2.clave && $scope.properties.dataToChange2.orden && $scope.properties.dataToChange2.descripcion && $scope.properties.dataToChange2.grupoBonita && $scope.properties.dataToChange2.urlAvisoPrivacidad  && $scope.properties.dataToChange2.calle && $scope.properties.dataToChange2.colonia && $scope.properties.dataToChange2.numeroExterior && $scope.properties.dataToChange2.numeroInterior && $scope.properties.dataToChange2.codigoPostal && $scope.properties.dataToChange2.municipio && $scope.properties.dataToChange2.pais && $scope.properties.dataToChange2.estados && $scope.properties.dataToChange2.email && $scope.properties.dataToChange2.urlImagen && ValidateEmail($scope.properties.dataToChange2.email) && !duplicados($scope.properties.dataToChange2.id,1,false,$scope.properties.dataToChange2.persistenceId) && !duplicados($scope.properties.dataToChange2.clave,2,false,$scope.properties.dataToChange2.persistenceId) && !duplicados($scope.properties.dataToChange2.orden,3,false,$scope.properties.dataToChange2.persistenceId)) {
		 if ($scope.properties.processId) {
		     $scope.properties.dataToChange2.pais.persistenceId_string = $scope.properties.dataToChange2.pais.persistenceId;
             var prom = doRequest('POST', '../API/extension/posgradosRest?url=insertCatCampus', $scope.properties.dataToChange2).then(function(response) {
                doRequest("GET", $scope.properties.url).then(function() {
                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                });
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
	   }else {
        let isError = false;
	    if(!$scope.properties.dataToChange2.grupoBonita ){
                swal("¡Aviso!", "Faltó capturar información en: Grupo relacionado", "warning");
                isError = true;
	    }
	    if(!ValidateEmail($scope.properties.dataToChange2.email)){
            swal("¡Aviso!", "El formato del correo eletrónico no es valido", "warning");
            isError = true;
		}
	    if(!$scope.properties.dataToChange2.email || $scope.properties.dataToChange2.email.trim().length <=0){
            swal("¡Aviso!", "Faltó capturar información en: Correo electronico", "warning");
            isError = true;
		}
		
        if(!$scope.properties.dataToChange2.urlImagen || $scope.properties.dataToChange2.urlImagen.trim().length <=0){
            swal("¡Aviso!", "Faltó capturar información en: Url de la imagen", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.estados ){
            swal("¡Aviso!", "Faltó capturar información en: Estado", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.pais ){
            swal("¡Aviso!", "Faltó capturar información en: País", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.municipio ){
            swal("¡Aviso!", "Faltó capturar información en: Municipio", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.codigoPostal ){
            swal("¡Aviso!", "Faltó capturar información en: Codigo postal", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.numeroInterior ){
            swal("¡Aviso!", "Faltó capturar información en: Numero interior", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.numeroExterior ){
            swal("¡Aviso!", "Faltó capturar información en: Numero exterior", "warning");
            isError = true;
        }
        if(!$scope.properties.dataToChange2.colonia ){
            swal("¡Aviso!", "Faltó capturar información en: Colonia", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.colonia ){
            swal("¡Aviso!", "Faltó capturar información en: Colonia", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.calle ){
            swal("¡Aviso!", "Faltó capturar información en: Calle", "warning");
            isError = true;
		}
		/*if(!$scope.properties.dataToChange2.urlAutorDatos ){
			swal("¡Aviso!", "Faltó capturar información en: Url Autor Datos", "warning");
		}
		if(!$scope.properties.dataToChange2.urlDatosVeridicos ){
			swal("¡Aviso!", "Faltó capturar información en: Url Datos verídicos", "warning");
		}*/
	    if(!$scope.properties.dataToChange2.urlAvisoPrivacidad ){
            swal("¡Aviso!", "Faltó capturar información en: Url aviso privacidad", "warning");
            isError = true;
	    }
	/*	if(!validateURL($scope.properties.dataToChange2.urlAvisoPrivacidad)){
			swal("¡Aviso!", "El formato de la Url aviso privacidad no es valido", "warning");
		}*/
	    
	    if(!$scope.properties.dataToChange2.descripcion ){
            swal("¡Aviso!", "Faltó capturar información en: Descripción", "warning");
            isError = true;
	    }
	    if(!$scope.properties.dataToChange2.orden){
                swal("¡Aviso!", "Faltó capturar información en: Orden", "warning");
                isError = true;
	    }
		
	    if(!$scope.properties.dataToChange2.clave){
            swal("¡Aviso!", "Faltó capturar información en: Clave", "warning");
            isError = true;
	    }
	    if(!$scope.properties.dataToChange2.id){
             swal("¡Aviso!", "Faltó capturar información en: Id", "warning");
             isError = true;
        }
        if(!isError){
            if(duplicados($scope.properties.dataToChange2.clave,2,false,$scope.properties.dataToChange2.persistenceId)){
                swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
            } else if(duplicados($scope.properties.dataToChange2.id,1,false,$scope.properties.dataToChange2.persistenceId)){
                 swal("¡Aviso!", "El id capturadao ya existe, por favor ingrese uno diferente.", "warning");
            } else if(duplicados($scope.properties.dataToChange2.orden,3,false,$scope.properties.dataToChange2.persistenceId)){
                swal("¡Aviso!", "El orden capturado ya existe, por favor ingrese uno diferente.", "warning");
            }
        }
        
	   }
			
	
	}else{
		if( $scope.properties.dataToChange2.lstCatCampusInput[0].id &&  $scope.properties.dataToChange2.lstCatCampusInput[0].clave && $scope.properties.dataToChange2.lstCatCampusInput[0].orden && $scope.properties.dataToChange2.lstCatCampusInput[0].descripcion && $scope.properties.dataToChange2.lstCatCampusInput[0].grupoBonita && $scope.properties.dataToChange2.lstCatCampusInput[0].urlAvisoPrivacidad && $scope.properties.dataToChange2.lstCatCampusInput[0].calle && $scope.properties.dataToChange2.lstCatCampusInput[0].colonia && $scope.properties.dataToChange2.lstCatCampusInput[0].numeroExterior && $scope.properties.dataToChange2.lstCatCampusInput[0].numeroInterior && $scope.properties.dataToChange2.lstCatCampusInput[0].codigoPostal && $scope.properties.dataToChange2.lstCatCampusInput[0].municipio && $scope.properties.dataToChange2.lstCatCampusInput[0].pais && $scope.properties.dataToChange2.lstCatCampusInput[0].estado && $scope.properties.dataToChange2.lstCatCampusInput[0].urlImagen && $scope.properties.dataToChange2.lstCatCampusInput[0].email  && ValidateEmail($scope.properties.dataToChange2.lstCatCampusInput[0].email) &&  !duplicados($scope.properties.dataToChange2.lstCatCampusInput[0].id,1,true) && !duplicados($scope.properties.dataToChange2.lstCatCampusInput[0].clave,2,true) && !duplicados($scope.properties.dataToChange2.lstCatCampusInput[0].orden,3,true) ){
			 if ($scope.properties.processId) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                doRequest("GET", $scope.properties.url).then(function() {
                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                });
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
	   }else {
        let isError = false;
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].grupoBonita ){
                swal("¡Aviso!", "Faltó capturar información en: Grupo relacionado", "warning");
                isError = true;
        }
        if(!ValidateEmail($scope.properties.dataToChange2.lstCatCampusInput[0].email)){
            swal("¡Aviso!", "El formato del correo eletrónico no es valido", "warning");
            isError = true;
		}
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].email && $scope.properties.dataToChange2.lstCatCampusInput[0].email.trim().length <= 0){
            swal("¡Aviso!", "Faltó capturar información en: Correo electrónico", "warning");
            isError = true;
		}
		
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].urlImagen && $scope.properties.dataToChange2.lstCatCampusInput[0].urlImagen.trim().length <= 0){
            swal("¡Aviso!", "Faltó capturar información en: URL de la imagen", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].estado ){
            swal("¡Aviso!", "Faltó capturar información en: Estado", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].pais ){
            swal("¡Aviso!", "Faltó capturar información en: País", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].municipio ){
            swal("¡Aviso!", "Faltó capturar información en: Municipio", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].codigoPostal ){
            swal("¡Aviso!", "Faltó capturar información en: Codigo postal", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].numeroInterior ){
            swal("¡Aviso!", "Faltó capturar información en: Numero interior", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].numeroExterior ){
            swal("¡Aviso!", "Faltó capturar información en: Numero exterior", "warning");
            isError = true;
        }
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].colonia ){
            swal("¡Aviso!", "Faltó capturar información en: Colonia", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].colonia ){
            swal("¡Aviso!", "Faltó capturar información en: Colonia", "warning");
            isError = true;
		}
        if(!$scope.properties.dataToChange2.lstCatCampusInput[0].calle ){
            swal("¡Aviso!", "Faltó capturar información en: Calle", "warning");
            isError = true;
		}
		/*if(!$scope.properties.dataToChange2.lstCatCampusInput[0].urlAutorDatos ){
			swal("¡Aviso!", "Faltó capturar información en: Url Autor Datos", "warning");
		}
		if(!$scope.properties.dataToChange2.lstCatCampusInput[0].urlDatosVeridicos ){
			swal("¡Aviso!", "Faltó capturar información en: Url Datos verídicos", "warning");
		}*/
		if(!$scope.properties.dataToChange2.lstCatCampusInput[0].urlAvisoPrivacidad ){
         swal("¡Aviso!", "Faltó capturar información en: Url aviso privacidad", "warning");
         isError = true;
		}
		/*if(!validateURL($scope.properties.dataToChange2.lstCatCampusInput[0].urlAvisoPrivacidad)){
			swal("¡Aviso!", "El formato de la Url aviso privacidad no es valido", "warning");
		}*/
	    
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].descripcion ){
            swal("¡Aviso!", "Faltó capturar información en: Descripción", "warning");
            isError = true;
	    }
	    /*if(!$scope.properties.dataToChange2.lstCatCampusInput[0].orden){
	 		   swal("¡Aviso!", "Faltó capturar información en: Orden", "warning");
	    }
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].clave){
	 	   swal("¡Aviso!", "Faltó capturar información en: Clave", "warning");
	    }
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].id){
	 		swal("¡Aviso!", "Faltó capturar información en: Id", "warning");
	    }*/
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].orden){
                swal("¡Aviso!", "Faltó capturar información en: Orden", "warning");
                isError = true;
	    }
	    
		
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].clave){
            swal("¡Aviso!", "Faltó capturar información en: Clave", "warning");
            isError = true;
	    }
		
	    if(!$scope.properties.dataToChange2.lstCatCampusInput[0].id){
             swal("¡Aviso!", "Faltó capturar información en: Id", "warning");
             isError = true;
        }
        if(!isError){
            if(duplicados($scope.properties.dataToChange2.lstCatCampusInput[0].orden,3,true)){
                swal("¡Aviso!", "El orden capturado ya existe, por favor ingrese uno diferente.", "warning");
            }
            if(duplicados($scope.properties.dataToChange2.lstCatCampusInput[0].clave,2,true)){
                swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
            }
            if(duplicados($scope.properties.dataToChange2.lstCatCampusInput[0].id,1,true)){
                 swal("¡Aviso!", "El id capturado ya existe, por favor ingrese uno diferente.", "warning");
            }
        }
        
	   }
    }	
}

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        vm.busy = true;
        var datos = angular.copy($scope.properties.dataToSend)
        datos.lstCatCampusInput[0].estado.persistenceId_string = datos.lstCatCampusInput[0].estado.persistenceId+"";
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function redirectIfNeeded() {
        var iframeId = $window.frameElement ? $window.frameElement.id : null;
        //Redirect only if we are not in the portal or a living app
        if (!iframeId || iframeId && iframeId.indexOf('bonitaframe') !== 0) {
            $window.location.assign($scope.properties.targetUrlOnSuccess);
        }
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return { 'user': userId };
        }
        return {};
    }

    /**
     * Extract the param value from a URL query
     * e.g. if param = "id", it extracts the id value in the following cases:
     *  1. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?id=8880000
     *  2. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en
     *  3. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en#hash=value
     * @returns {id}
     */
    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }

    function submitTask() {
        var id;
        id = getUrlParam('id');
        if (id) {
            var params = $scope.properties.userId;
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }
	
	function ValidateEmail(mail) 
	{
		let res = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        return res.test(mail);
	}

	function validateURL(textval) {
		var urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
		return urlregex.test(textval);
	}
	
	function duplicados(datos,eleccion,tipo,id){
		var i = 0, iguales = false, count = 0;
		switch (eleccion){
			case 1:
				for (i = 0; i < $scope.properties.lstContenido.length; i++) {
					if(tipo){
						if( $scope.properties.lstContenido[i].id !=undefined && $scope.properties.lstContenido[i].id != null && $scope.properties.lstContenido[i].id.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
							count++;
						}
					}else{
						
						if($scope.properties.lstContenido[i].persistenceId != id && $scope.properties.lstContenido[i].id !=undefined && $scope.properties.lstContenido[i].id != null && $scope.properties.lstContenido[i].id.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
							count++;
						}
					}
				}
				iguales =  (count>0 ?true:false);
			break;
			case 2:
				for (i = 0; i < $scope.properties.lstContenido.length; i++) {
					if(tipo){
						if( $scope.properties.lstContenido[i].clave !=undefined && $scope.properties.lstContenido[i].clave != null && $scope.properties.lstContenido[i].clave.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
							count++;
						}
					}else{
						
						if($scope.properties.lstContenido[i].persistenceId != id && $scope.properties.lstContenido[i].clave !=undefined && $scope.properties.lstContenido[i].clave != null &&$scope.properties.lstContenido[i].clave.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
							count++;
						}
					}
				}
				iguales =  (count>0 ?true:false);
			break;
			case 3:
				for (i = 0; i < $scope.properties.lstContenido.length; i++) {
					if(tipo){
						if($scope.properties.lstContenido[i].orden !=undefined && $scope.properties.lstContenido[i].orden != null && $scope.properties.lstContenido[i].orden.toString().normalize() === datos.toString().normalize()){
							count++;
						}
					}else{
						if($scope.properties.lstContenido[i].persistenceId != id && $scope.properties.lstContenido[i].orden !=undefined && $scope.properties.lstContenido[i].orden != null && $scope.properties.lstContenido[i].orden.toString().normalize() === datos.toString().normalize()){
							count++;
						}
					}
				}
				iguales =  (count>0 ?true:false);
			break;
		}
		
		return iguales;
	}
    

}