function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
     html5QrCode.stop().then(ignore => {
  // QR Code scanning is stopped.
  console.log("QR Code scanning stopped.");
}).catch(err => {
  // Stop failed, handle it.
  console.log("Unable to stop scanning.");
});
    };

}
