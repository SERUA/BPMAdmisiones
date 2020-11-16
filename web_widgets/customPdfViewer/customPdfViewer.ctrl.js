function ($scope, $sce) {
    $scope.pdfUrl = "";
     $scope.$watch(function(){
        return $scope.properties.contentStorageId;
    }, function (newValue, oldValue){
        $scope.pdfUrl =$sce.trustAsResourceUrl($scope.properties.contentStorageId);
        if($scope.pdfUrl === ""){
            $scope.properties.oculto = "true";
        }
    });
}