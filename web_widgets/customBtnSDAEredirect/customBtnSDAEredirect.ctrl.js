function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() { 
        let url = window.location.protocol + "//" + window.location.hostname + $scope.properties.url;
        window.location.replace(url);
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if(shouldClose)
        {modalService.close();}
    }

}
