function duplicados($scope, modalService) {

    const Estados = {
        ConDuplicados: "con-duplicados",
        SinDuplicados: "sin-duplicados",
        SinInformacion: "sin-informacion",
    };
    
    $scope.estado = Estados.SinInformacion;

    $scope.$watch("properties.matchingData", function(){
        const data = $scope.properties.matchingData;

        if (data && Array.isArray(data)) {
            if (data.length > 1) {
                $scope.estado = Estados.ConDuplicados;
                $scope.properties.value = false;
            }
            else if (data.length === 1) {
                $scope.estado = Estados.SinDuplicados;
                $scope.properties.value = true;
            }
            else {
                $scope.estado = Estados.SinInformacion;
                $scope.properties.value = false;
            }
        }
        else {
            $scope.estado = Estados.SinInformacion;
            $scope.properties.value = false;
        }
    });

    this.showModal = function showModal() {
        console.log("Entre")
        debugger;
        modalService.open($scope.properties.modalid);
    }
}