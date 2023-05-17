function ($scope, $http) {
    
    function getDocumentosAval(){
        let url  = "../API/extension/AnahuacBecasRestGET?url=getDocumentosByTipoApoyo&p=0&c=0&idTipoApoyo=" + $scope.properties.idTipoApoyo + "&campus=" + $scope.properties.campus + "&isAval=true";
        
        $http.get(url).success((result)=>{
            let documentos = [];
            $scope.properties.lstDocumentosAval = [];
            for(let documento of result.data){
                let objeto = {
                    "caseId": $scope.properties.caseId,
                    "catManejoDocumentos_id": documento.persistenceId,
                    "urlDocumento":"",
                    "catManejoDocumentos": documento,
                    "persistenceId_string": null
                }
                
                $scope.properties.lstDocumentosAval.push(objeto);
            }
        }).error((err)=>{
            
        });
    }
    
    $scope.$watch("properties.idTipoApoyo", ()=>{
        if($scope.properties.idTipoApoyo && $scope.properties.campus && $scope.properties.caseId){
            getDocumentosAval();
        }
    });
    
    $scope.$watch("properties.campus", ()=>{
        if($scope.properties.idTipoApoyo && $scope.properties.campus && $scope.properties.caseId){
            getDocumentosAval();
        }
    });
    
    $scope.$watch("properties.caseId", ()=>{
        if($scope.properties.idTipoApoyo && $scope.properties.campus && $scope.properties.caseId){
            getDocumentosAval();
        }
    });
    
}