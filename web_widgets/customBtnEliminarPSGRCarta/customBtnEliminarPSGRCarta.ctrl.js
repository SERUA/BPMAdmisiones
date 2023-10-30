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

    } else if ($scope.properties.action === 'Start process') {
      startProcess();
    } else if ($scope.properties.action === 'Submit task') {
      submitTask();
    } else if ($scope.properties.action === 'Open modal') {
      closeModal($scope.properties.closeOnSuccess);
      openModal($scope.properties.modalId);
    } else if ($scope.properties.action === 'Close modal') {
        debugger;
      closeModal(true);
      $scope.handleTrashClick($scope.properties.selectedDataPID);
      debugger;
      $scope.submitUpdatedData();
    } else if ($scope.properties.url) {
      doRequest($scope.properties.action, $scope.properties.url);
    }
  };

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
        $scope.properties.dataFromSuccess = data;
        $scope.properties.responseStatusCode = status;
        $scope.properties.dataFromError = undefined;
        notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
        if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
          redirectIfNeeded();
        }
        closeModal($scope.properties.closeOnSuccess);
      })
      .error(function(data, status) {
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
    id = getUrlParam('id');
    if (id) {
      var params = getUserParam();
	    params.assign = $scope.properties.assign;
      doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
        localStorageService.delete($window.location.href);
      });
    } else {
      $log.log('Impossible to retrieve the task id value from the URL');
    }
  }

  $scope.handleTrashClick = function (selectedDataPID) {
    debugger;
    var persistenceid = selectedDataPID.persistenceId;
    $scope.deleteCatalogo({ persistenceid: persistenceid })
        .then(function () {
            // Actualizar la matriz properties.content después de eliminar el registro
            var index = $scope.properties.content.indexOf(selectedDataPID);
            if (index !== -1) {
                $scope.properties.content.splice(index, 1);
            }
            $scope.submitUpdatedData(); // Obtener datos actualizados después de la eliminación
        });
};

$scope.submitUpdatedData = function () {
    debugger;

    // Realizar una solicitud GET a la API
    $http.get('../API/extension/posgradosRestGet?url=getCartasNotificaciones&p=0&c=100&campus=' + $scope.properties.campusSelected)
    .then(function (response) {
        // Actualizar contenido con los datos de la respuesta
        $scope.properties.contenido = response.data; // Actualiza contenido con los datos de la respuesta
    })
    .catch(function (error) {
        // Manejar errores en caso de que la solicitud falle
        console.error('Error al obtener los datos: ' + error);
    });
};

$scope.deleteCatalogo = function (dataToDelete) {
    debugger;
    // Realiza la solicitud HTTP para eliminar el registro utilizando la matriz JSON
    $scope.busy = true;

    return $http({
        method: 'POST',
        url: $scope.properties.urlDelete,
        data: dataToDelete,
        headers: { 'Content-Type': 'application/json;charset=utf-8' }
    }).then(function (response) {
        // Procesa la respuesta de eliminación si es necesario
        swal("OK", "Registro eliminado correctamente", "success");
        // Actualiza la vista o realiza otras acciones necesarias después de la eliminación
    }).catch(function (error) {
        swal("¡Algo ha fallado!", error.data.error, "error");
    }).finally(function () {
        $scope.busy = false;
    });
};

}
