function capitalizeName(name="") {
    name = name.toLowerCase();
    let nombres =  name.split(" ");
    let final = "";
    if(nombres.length>1){
        for(let i = 0; i<nombres.length; i++){
            let conectores = ["del", "de", "los", "la", "las","y"]
            if(!conectores.includes(nombres[i])){
                if(nombres[i].length > 1){
                    final += (final.length>0?" ":"")+nombres[i].charAt(0).toUpperCase() + nombres[i].slice(1).toLowerCase();
                }else{
                    final +=(final.length>0?" ":"")+nombres[i];
                }
            }else{
                final +=(final.length>0?" ":"")+nombres[i];
            }
        }    
    }else{
        final = name.charAt(0).toUpperCase() + name.slice(1).toLowerCase()
    }
    
    return final;
}

function capitalizeFirstLetter(string = "") {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}