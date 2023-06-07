function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {

    'use strict';

    var vm = this;
    $scope.generatePDF = function() {
    var element = document.querySelector($scope.properties.elementSelector);
    var opt = {
            margin: [15, 20, 15, 20],
            filename: $scope.properties.fileName + ".pdf",
            image: { type: 'jpg', quality: 0.98 },
            html2canvas: { dpi: 100, letterRendering: true, useCORS: true },
            jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' },
            pagebreak: { mode: ['css'] }
            // pagebreak: { avoid: '.avoid' }
        };

        // html2pdf(element, opt); 
        function getPDF(element, opt) {
            var defered=$q.defer();
            var promise=defered.promise;
            html2pdf(element, opt); 
            return promise;
        }

        var promise=getPDF(element, opt);
        promise.then(function(resultado) {
            alert("Fin de la promesa");
            blockUI.stop();
        }, function(error) {
            $scope.mensaje="Se ha producido un error al obtener el dato:"+error;
        });
    }

}