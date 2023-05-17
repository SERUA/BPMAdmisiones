function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    $scope.action = function() {
        if ($scope.properties.isValidStep) {
            // $scope.properties.selectedIndex++;
            // blockUI.start();
            // submitTask();
            modalService.open($scope.properties.idModal);
        } else {
            swal($scope.properties.messageTitle, $scope.properties.errorMessage, "warning");
        }
    }

    function submitTask() {
        var id;
        id = $scope.properties.taskId;
        if (id) {
            doRequest('POST', '../API/bpm/userTask/' + id + '/execution').then(function() {
                
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

    function doRequest(method, url) {
        let dataToSend = angular.copy($scope.properties.formOutput);
        var req = {
            method: method,
            url: url,
            data: dataToSend
        };

        return $http(req)
            .success(function(data, status) {
                let newUrl = "/portal/resource/app/aspiranteSDAE/solicitud_apoyo_iniciada/content/?app=aspiranteSDAE";
                window.location.replace(newUrl);
            })
            .error(function(data, status) {
                console.log("task failed")
            });
    }
}