function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if($scope.properties.assign ){
            assignTask();   
        } else {
            submitTask();
        }
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if(shouldClose)
            modalService.close();
    }

    /**
       * Execute a get/post request to an URL
       * It also bind custom data from success|error to a data
       * @return {void}
       */
    function assignTask() {
        vm.busy = true;
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;
        
        var req = {
            method: "PUT",
            url: url,
            data:{
                "assigned_id": $scope.properties.userId
            }
        };

        return $http(req).success(function(data, status) {
            submitTask();
        })
        .error(function(data, status) {
            swal("Error", data.message, "error")
        })
        .finally(function() {
            vm.busy = false;
        });
    }
    
    function submitTask() {
        let url = '../API/bpm/userTask/' + $scope.properties.taskId + '/execution';
        var req = {
            method: "POST",
            url: url,
            data: $scope.properties.contractObject
        };

        return $http(req).success(function(data, status) {
        //   window.location.reload();
            insertBitacora();
        })
        .error(function(data, status) {
       
        })
        .finally(function() {
            vm.busy = false;
        });
    }
    
     function insertBitacora() {
        let url = $scope.properties.urlBitacora;
        let dataToSend = angular.copy($scope.properties.objetoBitacora);

        $http.post(url, dataToSend).success(function () {
            debugger;
        }).error(function () {
            debugger;
        }).finally(function () {
            window.location.reload();
        });
    }
    
}
