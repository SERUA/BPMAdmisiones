function PbInputCtrl($scope, $log, widgetNameFactory, $sce) {

    'use strict';

    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }

    $scope.currentProjectUrl = "";

    $scope.cargarVideo = function () {
        if ($scope.properties.value.includes("https://www.youtube.com")) {
            $scope.currentProjectUrl = $sce.trustAsResourceUrl($scope.properties.value.replace("watch?v=", "embed/"));
        } else {
            Swal.fire(
                'Aviso',
                'El link del video introducido no pertenece a la plataforma de YouYube favor de cambiarlo',
                'warning'
            );

            $scope.currentProjectUrl = "";
            $scope.properties.value = "";
        }
    }

    $scope.$watch("properties.value", ()=>{
        if($scope.properties.value){
            $scope.cargarVideo();
        }
    });
}