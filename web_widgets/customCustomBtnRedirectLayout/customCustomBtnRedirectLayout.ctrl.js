function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.redirect = function(){
        let href = window.location.protocol + "//" + window.location.host + $scope.properties.url;
        window.top.location = href;
    }
}
