function ($scope) {
    $scope.deleteContent = function(objContent) {
        console.log(objContent);
        var index = $scope.properties.content.indexOf(objContent);
        console.log(index);
        if(index != -1){
            $scope.properties.content.splice(index, 1);
        }
    }
}