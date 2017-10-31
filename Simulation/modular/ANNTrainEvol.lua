
function ModifyAnnWeights(ann,weights)

    for i=1,#ann do
        for j=1,#ann[i] do
            for k=1,#ann[i][j] do
               ann[i][j][k] = weights[i][j][k]
            end
        end
    end        
    
end


function createANNfromWeightsList(morpho,weightList)
    
    local ann = inittableoftables(#morpho-1)
    
    local k=1
    
    for i=1,#morpho-1 do
        ann[i] = inittableoftables(morpho[i+1])
        for j=1,morpho[i+1] do
            for p=1,morpho[i] do
                ann[i][j][p] = weightList[k]
                k=k+1
            end  
        end
        
    end
    
    return ann

end

function getWeightListfromANN(ann)

    local weightList = {}
    
    p=1
    
    for i=1,#ann do
        for j=1,#ann[i] do
            for k=1,#ann[i][j] do
               weightList[p] = ann[i][j][k]
               p=p+1
            end
        end
    end      

    return weightList

end 