function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;
  
  $scope.registrarBonita = function() {
        var item = {};
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=encode&p=0&c=10",
            data: angular.copy(item)
        };

        return $http(req)
            .success(function(data, status) {
                var b64 = data.data;
                var bin = atob(b64);
                
                /*// Embed the PDF into the HTML page and show it to the user
                var obj = document.createElement('object');
                obj.style.width = '100%';
                obj.style.height = '842pt';
                obj.type = 'application/pdf';
                obj.data = 'data:application/pdf;base64,' + b64;
                document.body.appendChild(obj);*/
                
                // Insert a link that allows the user to download the PDF file
                var link = document.createElement('a');
                link.innerHTML = 'Download PDF file';
                link.download = $scope.properties.nombreArchivo.trim()+'.pdf';
                link.href = 'data:application/octet-stream;base64,' + b64;
                link.target="_blank";
                link.style="visibility: hidden;";
                link.id="myCheck"
                document.body.appendChild(link);
                document.getElementById("myCheck").click();
            })
            .error(function(data, status) {
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

}
