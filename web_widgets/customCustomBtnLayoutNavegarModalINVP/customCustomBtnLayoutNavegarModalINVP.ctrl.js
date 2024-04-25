function PbButtonCtrl($scope, modalService) {
    $scope.changeVariable = function(){
        let idioma = angular.copy($scope.properties.idioma);
        
        swal({
            title: idioma === "ESP" ? "Confirmación" : "Confirmation", 
            text: idioma === "ESP" ? "Una vez que envies el examen, no podrás cambiar o concluir tus respuestas" : "Once you send your exam, you won't be able to change or finish your answers",
            icon: "warning",
            // buttons: [
            //     'Cancelar',
            //     'Enviar todo y terminar'
            // ]
            buttons: {
                no: {
                    text: idioma === "ESP" ? "Cancelar" : "Cancel",
                    value: false,
                    className:'btn-info'
                },
                si: {
                    text: idioma === "ESP" ? "Enviar todo y terminar" : "Send and finish",
                    value: true,
                    className:'btn-primary'
                }
            }
        }).then(function (isConfirm) {
            if (isConfirm) {
                localStorage.setItem('terminado', "true");
                $scope.properties.navigationVar = $scope.properties.newValue;
                modalService.close();
            } else {
                modalService.close();
            }
        })
            
        // localStorage.setItem('terminado', "true");
        // $scope.properties.navigationVar = $scope.properties.newValue;
    }   
}