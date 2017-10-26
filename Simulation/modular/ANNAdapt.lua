require "ANN"
require "ANNTrain"

math.randomseed(os.time())


annLayers = {3,5,5,2}

ann = createANN(annLayers)

inputs = {0.2,0.8,0.33}

output = propagateANN(ann,inputs)

for k,v in pairs(output) do print(k,v) end


function modifytable(table1)
    
    table1[1] = 11
    
end

table2 = {1,5,6,9}

modifytable(table2)

for k,v in pairs(table2) do print(k,v) end

annLayers = {2,3,4,1}

ann = createANN(annLayers)

inputs = {1,0}

output = propagateANN(ann,inputs)

for k,v in pairs(output) do print(k,v) end

inputs = {{0,0},{0,1},{1,0},{1,1}}
outputs = {{1},{0},{0},{1}}
params = {{0.7,0.3},{0.5,0.5},{0.1,0.9}}
print('-----------------------------')

for j=1,#inputs do
         realoutput = propagateANN(ann,inputs[j])
         for k,v in pairs(realoutput) do print(k,v) end
end

for p=1,#params do
    for i=1,1000 do
        for j=1,#inputs do

            trainANN(ann,inputs[j],outputs[j],params[p])
        end

    end
    print('-----------------------------')
for j=1,#inputs do
         realoutput = propagateANN(ann,inputs[j])
         for k,v in pairs(realoutput) do print(k,v) end
end 
end
  
