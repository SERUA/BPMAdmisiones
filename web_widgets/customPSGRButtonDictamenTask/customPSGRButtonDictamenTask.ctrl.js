function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
    
    // Verificar incumplimiento
    if ($scope.properties.showNonComplianceMessage) {
        let title = "Titulo";
        let text = "Sin mensaje";
        let icon = null;
        try {
            if ($scope.properties.nonComplianceMessage.title) title = $scope.properties.nonComplianceMessage.title;
            if ($scope.properties.nonComplianceMessage.text) text = $scope.properties.nonComplianceMessage.text;
            icon = $scope.properties.nonComplianceMessage.icon && ['success', 'info', 'warning', 'error'].includes($scope.properties.nonComplianceMessage.icon) ? $scope.properties.nonComplianceMessage.icon : null;    
        }
        catch(e) {}
        
        Swal.fire({
            title: title,
            text: text,
            icon: icon,
            confirmButtonText: "Aceptar",
        });
        return;
    }
      
    // Realizar acción
    if ($scope.properties.action === 'Remove from collection') {
      removeFromCollection();
      closeModal($scope.properties.closeOnSuccess);
    } else if ($scope.properties.action === 'Add to collection') {
      addToCollection();
      closeModal($scope.properties.closeOnSuccess);
    } else if ($scope.properties.action === 'Start process') {
      startProcess();
    } else if ($scope.properties.action === 'Submit task') {
      submitTask();
    } else if ($scope.properties.action === 'Open modal') {
      closeModal($scope.properties.closeOnSuccess);
      openModal($scope.properties.modalId);
    } else if ($scope.properties.action === 'Close modal') {
      closeModal(true);
    } else if ($scope.properties.url) {
      //doRequest($scope.properties.action, $scope.properties.url);
    }
  };
  
  function addCaseComment() {
    const dataToSend = {
      content: JSON.stringify($scope.properties.caseUserCommentToSend),
      processInstanceId: $scope.properties.caseId,
    };
    doRequest("POST","/API/bpm/comment/", null, dataToSend, 
      function(data, status) { },
      function(data, status) { console.log("No se agregó el comentario de caso.") }  
    );
  }

  function enviarCarta() {
    doRequest("POST","/API/extension/posgradosRest?url=generateHtml", null, $scope.properties.cartaDatos, 
      function(data, status) {
        $scope.properties.taskResultMessages.push("✔ El correo se envió correctamente.")
       },
      function(data, status) { 
        $scope.properties.taskResultMessages.push("✖ Algo falló al enviar el correo.")
      }  
    );
  }

  // Ejecución de tarea Dictamen (Admisión, No admisión o Archivar)
  function submitTask() {
    var id;
    id = $scope.properties.taskId;
    if (id) {
      var params = {};
	  params.assign = true;

      doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params, $scope.properties.dataToSend,
        // Success callback
        (data, status) => {
          $scope.properties.dataFromSuccess = data;
          $scope.properties.responseStatusCode = status;
          $scope.properties.dataFromError = undefined;
          $scope.properties.taskCompleted = true;
          notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
          if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
            redirectIfNeeded();
          }
          closeModal($scope.properties.closeOnSuccess);
          // Enviar carta
          if ($scope.properties.cartaToSend) {
            enviarCarta();   
          }
          // Enviar comentario de caso
          if ($scope.properties.caseUserCommentToSend && $scope.properties.caseUserCommentToSend.mensaje !== null) {
            addCaseComment();
          }
          
        // Error callback
      }, (data, status) => {
        $scope.properties.dataFromError = data;
        $scope.properties.responseStatusCode = status;
        $scope.properties.dataFromSuccess = undefined;
        $scope.properties.taskCompleted = false;
        notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
      })
      .then(function() {
        localStorageService.delete($window.location.href);
      });
    } 
    else {
      $log.log('Impossible to retrieve the task id value from the URL');
    }
  }

  function openModal(modalId) {
    modalService.open(modalId);
  }

  function closeModal(shouldClose) {
    if(shouldClose)
    {modalService.close();}
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
    var id = $scope.properties.taskId;
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
  function doRequest(method, url, params, dataToSend, successCallback, errorCallback) {
    vm.busy = true;
    var req = {
      method: method,
      url: url,
      data: angular.copy(dataToSend),
      params: params
    };

    return $http(req)
      .success(successCallback)
      .error(errorCallback)
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

}
