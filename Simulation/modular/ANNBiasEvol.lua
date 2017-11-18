function createANNfromWeightsList(layersizes,weightlist)
    
    local ann = {}
    
    local k = 1 
    local layer = {}
    
    for i=2,#layersizes do
        layer,k = createLayerfromWeightList(layersizes[i-1],layersizes[i],weightlist,k)
        --print(k)
        table.insert(ann,layer)
    end

    return ann     


end

function createLayerfromWeightList(inputs,outputs,weightlist,k)

    local kl = k

    local layer = {}
    local perceptron = {}
    
    for i=1,outputs do
        perceptron,kl = createPerceptronfromWeightList(inputs,weightlist,kl)
        table.insert(layer,perceptron)
    end
    
    return layer,kl


end

function createPerceptronfromWeightList(size,weightlist,kl)
    
    local kp = kl
    local weights = {}
    local bias = weightlist[kp]
    kp = kp + 1
    
    for i=1,size do 
        local weight = weightlist[kp]
        kp = kp + 1
        table.insert(weights,weight)
    end
    
   
    return {weights,bias},kp
    
end

function getWeightListfromANN(ann)
    
    local weightlist = {}
    
    for i=1,#ann do
        local layerweightlist = getWeightListfromLayer(ann[i])
        for j=1,#layerweightlist do
            table.insert(weightlist,layerweightlist[j])
        end
    end
    
    return weightlist
    
end

function getWeightListfromLayer(layer)

    local weightlist = {}
    
    for i=1,#layer do
        local perceptronWeightList = getWeightListfromPerceptron(layer[i])
        for j=1,#perceptronWeightList do
            table.insert(weightlist,perceptronWeightList[j])
        end
    end
    
    return weightlist

end

function getWeightListfromPerceptron(perceptron)

    local weightlist = {}
    
    table.insert(weightlist,perceptron[2])
    
    for j=1,#perceptron[1] do
        table.insert(weightlist,perceptron[1][j])
    end
    
    for k,v in pairs(weightlist) do print(k,v) end
    
    return weightlist

end

function printWeightListformANN(ann)

    local weightlist = getWeightListfromANN(ann)
    
    str = ""
    
    for i=1,#weightlist do
        str = str..weightlist[i]..", "
    end
    
    print(#weightlist)
    print(str)
    
end

