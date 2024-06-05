function PbLinkCtrl($scope, $location, $window, httpParamSerializer) {
    $scope.descargarArchivoBase64 = function () {
        var archivoBase64 = $scope.properties.targetUrl;
        var enlaceDescarga = document.createElement("a");
        enlaceDescarga.href = archivoBase64.linkSource;
        enlaceDescarga.download = archivoBase64.fileName;
        enlaceDescarga.target = "_blank"; 
        
        document.body.appendChild(enlaceDescarga);
        enlaceDescarga.click();
        document.body.removeChild(enlaceDescarga);
    }
}