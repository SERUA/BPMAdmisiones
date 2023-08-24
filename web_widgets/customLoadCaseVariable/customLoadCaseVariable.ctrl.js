function loadCaseVariableCtrl($scope, $http) {
    $scope.lstArchivedCase = [];

    $scope.loadCaseVariable = function(caseId) {
        debugger;
        doRequest("GET", "../API/extension/RegistroRest?url=caseVariable&caseid=" + caseId, {},
            function(data, status) { //SUCCESS
                $scope.properties.caseVariable = data;
            },
            function(data, status) { //ERROR
                console.log("----------------------------------ERROR----------------------------------");
                console.log(data);
                console.log("status: " + status);
                console.log("--------------------------------FIN ERROR--------------------------------");
                $scope.loadArchivedCaseVariable(caseId);
            })
    }

    $scope.loadArchivedCaseVariable = function(caseId) {
        doRequest("GET", "../API/extension/AnahuacRestGet?url=archivedCaseVariable&p=0&c=9999&caseId=" + caseId, {},
            function(data, status) { //SUCCESS
                $scope.properties.caseVariable = data.data;
            },
            function(data, status) { //ERROR
                console.log("----------------------------------ERROR----------------------------------");
                console.log(data);
                console.log("status: " + status);
                console.log("--------------------------------FIN ERROR--------------------------------");
            })
    }

    function doRequest(method, url, dataToSend, callback, errorCallback) {

        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend)
        };

        return $http(req)
            .success(callback)
            .error(errorCallback);
    }

    $scope.$watchCollection("properties.caseId", function(newValue, oldValue) {
        if ($scope.properties.caseId !== undefined) {
            $scope.loadCaseVariable($scope.properties.caseId);
        }
    });
}