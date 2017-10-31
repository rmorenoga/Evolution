require "ANN"

function trainANN(ann,input,desiredoutput,params)

    local outputs,weightedsums = setup(ann,input)
    local errors = backpropagateerror(ann,outputs,weightedsums,input,desiredoutput,params)
    for i=1,#ann do
        trainLayer(ann[i],errors[i],outputs[i],params)
    end
    
end

function setup(ann,input)

    local outputs = {}
    local weightedsums = {}
    local layerweightedsums, layeroutput
    
    table.insert(outputs,input)
    
    for i=1,#ann do 
        layerweightedsums, layeroutput = layersetup(ann[i],outputs[i])           
        table.insert(weightedsums,layerweightedsums)
        table.insert(outputs,layeroutput)
    end
    
    --print(#ann)
    --for i=1,#outputs do
     --   print(#outputs[i])
   -- end
        
    
    return outputs,weightedsums
    
end
    
    
function layersetup(layer,input)
    
    local weightedsums = {}
    local outputs = {}

    for i=1,#layer do
        table.insert(weightedsums,calculateweightedsum(layer[i],input))
        table.insert(outputs,activate(weightedsums[i]))
    end
        
    return weightedsums, outputs
    

end

function backpropagateerror(ann,outputs,weightedsums,input,desired,params)

    local errors = inittableoftables(#ann)
    errors[#ann] = calculateOutputLayerError(outputs[#ann],weightedsums[#ann],outputs[#ann+1],desired,params)
    for i=#ann-1,1,-1 do
        errors[i] = backpropagaterrorLayer(ann[i+1],weightedsums[i],errors[i+1])
    end
    
    return errors
end

function inittableoftables(size)
    local matrix = {}
    for i=1,size do
        matrix[i] = {}
    end
    
    return matrix
end

function crossEntropyError(input,weightedsum,output,desired,params)  
    local errors = {}
    for i=1,#desired do
        table.insert(errors,output[i]-desired[i])
        local sum = 0
        for j=1,#input do
            sum = sum + (input[j]*errors[i])
        end
        if (errors[i] > 0) then 
            sum = sum*params[2]
        end
        errors[i] = sum*derivate(weightedsum[i])     
    end
        
    return errors

end

function SE(input,weightedsum,output,desired,params)
   local errors = {}
   for i=1,#desired do
        table.insert(errors,output[i]-desired[i])
        
   end
   
   return errors

end

function calculateOutputLayerError(input,weightedsum,output,desired,params)
    
    --return crossEntropyError(input,weightedsum,output,desired,params)
    return SE(input,weightedsum,output,desired,params)

end

function backpropagaterrorLayer(layer,weightedsum,errors)
    
    local backerror = {}
    for i=1,#weightedsum do
        table.insert(backerror,0)
        for j=1,#layer do
            backerror[i] = backerror[i] + (errors[j]*layer[j][i])
        end
        backerror[i] = backerror[i]*derivate(weightedsum[i])
    end
    return backerror
end

        
function trainLayer(layer,errors,inputs,params)

    for i=1,#layer do
        for j=1,#layer[i] do
            layer[i][j] = layer[i][j] - (params[1]*errors[i]*inputs[j])
        end
    end            

end
        