function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI, $filter) {

    'use strict';
  
    var vm = this;
     
    this.action = function action() {
      if ($scope.properties.action === 'Remove from collection') {
        removeFromCollection();
        closeModal($scope.properties.closeOnSuccess);
      } else if ($scope.properties.action === 'Add to collection') {
        addToCollection();
        closeModal($scope.properties.closeOnSuccess);
      } else if ($scope.properties.action === 'Start process') {
          if(!$scope.properties.formInput.catSolicitudDeAdmisionInput.datosVeridicos){
            swal($filter('translate')("¡Aviso!"), $filter('translate')("Debes aceptar que los datos ingresados son verídicos"), "warning");
        } else if (!$scope.properties.formInput.catSolicitudDeAdmisionInput.aceptoAvisoPrivacidad) {
            swal($filter('translate')("¡Aviso!"), $filter('translate')("Debes aceptar el aviso de privacidad"), "warning");
        } else if (!$scope.properties.formInput.catSolicitudDeAdmisionInput.confirmarAutorDatos) {
            swal($filter('translate')("¡Aviso!"), $filter('translate')("Debes aceptar que confirma que eres el autor de los datos de este formulario"), "warning");
        } else {
            startProcess();
        }
        } else if ($scope.properties.action === 'Submit task') {
            console.log("Sending for modification");
            if (!$scope.properties.catSolicitudDeAdmision.datosVeridicos) {
                swal($filter('translate')("¡Aviso!"), $filter('translate')("Debes aceptar que los datos ingresados son verídicos"), "warning");
            } else if (!$scope.properties.catSolicitudDeAdmision.aceptoAvisoPrivacidad) {
                swal($filter('translate')("¡Aviso!"), $filter('translate')("Debes aceptar el aviso de privacidad"), "warning");
            } else if (!$scope.properties.catSolicitudDeAdmision.confirmarAutorDatos) {
                swal($filter('translate')("¡Aviso!"), $filter('translate')("Debes aceptar que confirma que eres el autor de los datos de este formulario"), "warning");
          }else{
              blockUI.start();
              $scope.properties.dataToSend.isEnviarSolicitudCont = true;
              $scope.properties.dataToSend.catSolicitudDeAdmisionInput.promedioGeneral = $scope.properties.dataToSend.catSolicitudDeAdmisionInput.promedioGeneral + "";
              $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.Bachilleratopersistenceid;
              $scope.assignTask();
              //submitTask();
          }
        
      } else if ($scope.properties.action === 'Open modal') {
        closeModal($scope.properties.closeOnSuccess);
        openModal($scope.properties.modalId);
      } else if ($scope.properties.action === 'Close modal') {
        closeModal(true);
      } else if ($scope.properties.url) {
        doRequest($scope.properties.action, $scope.properties.url);
      }
    };
  
      $scope.assignTask = function () {
          //$scope.showModal();
          var req = {
            method: "GET",
            url: "../API/system/session/unusedid",
        };
        return $http(req).success(function(data, status) {
            let url = "../API/bpm/userTask/" + $scope.properties.taskId;
          if($scope.properties.catSolicitudDeAdmision.correoElectronico != data.user_name){
              swal("¡Error!", "Su sesion ha expirado", "warning");   
              setTimeout(function(){ window.top.location.href = $scope.properties.urlDireccion }, 3000);
          }else{
              var req = {
                  method: "PUT",
                  url: url,
                  data:{
                      "assigned_id": $scope.properties.userId
                  }
              };
      
              return $http(req).success(function(data, status) {
                  //$scope.executeTask();
                  submitTask();
              })
              .error(function(data, status) {
                  $scope.hideModal();
                  swal("Error", data.message, "error");
              })
              .finally(function() {
                  
              }); 
          }
        })
        .error(function(data, status) {
            swal("¡Error!", data.message, "error");
        })
        .finally(function() {

        });  
          
          
      }
  
    function openModal(modalId) {
      modalService.open(modalId);
    }
  
    function closeModal(shouldClose) {
      if(shouldClose)
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
      var id = getUrlParam('id');
      if (id) {
        var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function() {
          localStorageService.delete($window.location.href);
        });
  
      } else {
        $log.log('Impossible to retrieve the process definition id value from the URL');
      }
    }
  
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
      vm.busy = true;
      var req = {
        method: method,
        url: url,
        data: angular.copy($scope.properties.dataToSend),
        params: params
      };
  
      return $http(req)
        .success(function(data, status) {
          setTimeout(function(){
              $scope.properties.dataFromSuccess = data;
              $scope.properties.responseStatusCode = status;
              $scope.properties.dataFromError = undefined;
              notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
              if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                      blockUI.stop();
                      redirectIfNeeded();
              }
              closeModal($scope.properties.closeOnSuccess);
              blockUI.stop();
          },5000)
        })
        .error(function(data, status) {
            blockUI.stop();
          $scope.properties.dataFromError = data;
          $scope.properties.responseStatusCode = status;
          $scope.properties.dataFromSuccess = undefined;
          notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
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
      //id = getUrlParam('id');
      id = $scope.properties.taskId;
      if (id) {
        var params = getUserParam();
          params.assign = $scope.properties.assign;
        doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params).then(function() {
            localStorageService.delete($window.location.href);
        });
      } else {
        $log.log('Impossible to retrieve the task id value from the URL');
      }
    }


    function doRequestFamilia() {
      let cantidad = ($scope.properties.dataToSend.tutorInput.length);
      let url = `../API/extension/AnahuacRestGet?url=getUpdateFamiliaresIntento&p=0&c=10&caseid=${$scope.properties.catSolicitudDeAdmision.caseId}&intentos=${$scope.properties.catSolicitudDeAdmision.countRechazos === null?0:$scope.properties.catSolicitudDeAdmision.countRechazos}&cantidad=${cantidad == null?0:cantidad}`;
      vm.busy = true;
        var req = {
          method: "GET",
          url: url
        };
    
        return $http(req)
          .success(function(data, status) {
              
            blockUI.stop();
          })
          .error(function(data, status) {
              blockUI.stop();
            
          })
          .finally(function() {
            vm.busy = false;
          });
      }

  
  }
  