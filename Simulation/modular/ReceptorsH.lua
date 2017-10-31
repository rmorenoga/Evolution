require('lua/modular/ANN')
require('lua/modular/ANNTrainEvol')
require('lua/modular/Convert')


function receptorsbase(hormones,ampd,offd,phasediff,v,deltaparam,delta)
	--print(delta)
    local ampdnew = ampd
    local offdnew = offd
    local vnew = v
    local phasediffnew ={}      --Phasediff
    for j=1,#phasediff do
        phasediffnew[j] = phasediff[j]
    end


    local ampset = {}
    local offsetset = {}
    local vset = {}
    local phasediffset = {}

    for i=1,7 do
        ampset[i] = deltaparam[i]
        offsetset[i] = deltaparam[i+7]
        vset[i] = deltaparam[i+42]
    end

    phasediffset[1]={}
    phasediffset[2]={}
    phasediffset[3]={}
    phasediffset[4]={}
    phasediffset[5]={}
    phasediffset[6]={}
    phasediffset[7]={}


    for i=1,4 do
        phasediffset[1][i] = deltaparam[i+14]
        phasediffset[2][i] = deltaparam[i+18]
        phasediffset[3][i] = deltaparam[i+22]
        phasediffset[4][i] = deltaparam[i+26]
        phasediffset[5][i] = deltaparam[i+30]
        phasediffset[6][i] = deltaparam[i+34]
        phasediffset[7][i] = deltaparam[i+38]
    end


    for k=1,#hormones do
        --print(hormones[k])
        if (hormones[k]~=-1) then
            --print('Received '..k)
            if(ampdnew<ampset[k]) then   --Amplitude
                ampdnew = ampdnew + (delta*hormones[k])
                if (ampdnew > 1) then ampdnew = 1 end
            elseif (ampdnew>ampset[k]) then
                ampdnew = ampdnew - (delta*hormones[k])
                if (ampdnew < -1) then ampdnew = -1 end
            end    
            if(offdnew<offsetset[k]) then  --Offset
                offdnew = offdnew + (delta*hormones[k])
                if (offdnew > 1) then offdnew = 1 end
            elseif (offdnew>offsetset[k]) then
                offdnew = offdnew - (delta*hormones[k])
                if (offdnew < -1) then offdnew = -1 end
            end    
            for i=1,#phasediffset[k] do
                if (phasediffnew[i]<phasediffset[k][i]) then
                    phasediffnew[i] = phasediffnew[i] + (delta*hormones[k])
                    if (phasediffnew[i] > math.pi) then phasediffnew[i] = math.pi end
                elseif (phasediffnew[i]>phasediffset[k][i]) then
                    phasediffnew[i] = phasediffnew[i] - (delta*hormones[k])
                    if (phasediffnew[i] < -math.pi) then phasediffnew[i] = -math.pi end
                end
            end
            if(vnew<vset[k]) then   --Frequency
                vnew = vnew + (delta*hormones[k])
                if (vnew >1) then vnew = 1 end
            elseif (vnew>vset[k]) then
                vnew = vnew - (delta*hormones[k])
                if (vnew <0) then vnew = 0 end
            end
        end
    end

    return ampdnew,offdnew,phasediffnew,vnew
end


function receptorsANNB(hormsum,ampd,offd,phasediff,v,ori,deltaparam)

    local ampdnew = ampd
    local offdnew = offd
    local vnew = v
    local phasediffnew ={}  --Phasediff

    for j=1,#phasediff do
        phasediffnew[j] = phasediff[j]
    end

    local annLayers = {13,8,6}

    local ann = createANNfromWeightsList(annLayers,deltaparam)

    local oriinputs = getAnnInputsfromOri(ori)

    local inputs = {} -- {ori inputs, hormone inputs}

    for i=1,#oriinputs do
        table.insert(inputs,oriinputs[i])
    end

    for i=1,#hormsum do
        table.insert(inputs,hormsum[i])
    end

    local outputs = propagateANN(ann,inputs) --{ampdnew,offdnew,phasediffnew}

    ampdnew,offdnew,phasediffnew = ConvertAnnOutputstoCPGParameters(outputs,#phasediff)

    return ampdnew,offdnew,phasediffnew,vnew

end

function receptorsANNLastTime(hormsum,ampd,offd,phasediff,v,ori,deltaparam)

    local ampdnew = ampd
    local offdnew = offd
    local vnew = v
    local phasediffnew ={}  --Phasediff

    for j=1,#phasediff do
        phasediffnew[j] = phasediff[j]
    end

    local annLayers = {19,10,6}

    local ann = createANNfromWeightsList(annLayers,deltaparam)

    local oriinputs = getAnnInputsfromOri(ori)

    local cpgInputs = ConvertCPGParameterstoAnnInputs(ampdnew,offdnew,phasediffnew)

    local inputs = {} -- {ori inputs, hormone inputs}

    for i=1,#oriinputs do
        table.insert(inputs,oriinputs[i])
    end

    for i=1,#hormsum do
        table.insert(inputs,hormsum[i])
    end

    for i=1,#cpgInputs do
        table.insert(inputs,cpgInputs[i])
    end

    print(#deltaparam)

    local outputs = propagateANN(ann,inputs) --{ampdnew,offdnew,phasediffnew}

    ampdnew,offdnew,phasediffnew = ConvertAnnOutputstoCPGParameters(outputs,#phasediff)

    return ampdnew,offdnew,phasediffnew,vnew    

end

function getAnnInputsfromOri(ori)

    local inputs = {}
    for i=1,6 do
        inputs[i] = 0.1
    end

    inputs[ori] = 0.9

    return inputs

end    

function integrate(hormones,count)
    --may overflow
    local countcopy = {}
        for i=1,#count do
            countcopy[i] = count[i]
        end
        for k=1,#hormones do
            if (hormones[k]~=-1) then
                countcopy[k] = countcopy[k]+1
            end
        end
    return countcopy
end

function sortbycount(count)
    local sorted = {}
    local countcopy = {}
    for i=1,#count do
        countcopy[i] = count[i]
    end

    table.sort(countcopy,function(a,b) return a>b end)
    --print('countcopy')
     --for k,v in pairs(countcopy) do print(k,v) end
     
     seq = {}
     seq[1] = 1
     k = 1
     for i=2,#countcopy do
       if (countcopy[i]~=countcopy[i-1]) then
         k=k+1
       end
       seq[i] = k
     end
     --print('seq')
     --for k,v in pairs(seq) do print(k,v) end

    local countinv = {}
    for k,v in pairs(countcopy) do

        countinv[v]=k

    end
  --print('countinv')
    --for k,v in pairs(countinv) do print(k,v) end

    for i=1,#count do
      sorted[i] = seq[countinv[count[i]]]
    end


    return sorted

end

function normalizedHSum(hormones,rhorm)

    local hormsum = {}
    local hormnorm = {}
    local count = {}

    for i=1,#hormones do
        count[i] = 0
        if (hormones[i]==-1) then
            hormsum[i] = 0
        else
            hormsum[i] = hormones[i]
        end
    end

    count = integrate(hormones,count)
   
    for i=1,#rhorm do
        if (#rhorm[i] > 0) then
            for j=1,#rhorm[i] do
                local exhorm = simUnpackFloatTable(rhorm[i][j]) 
                count = integrate(exhorm,count)
                for k=1,#exhorm do
                    if (exhorm[k]~=-1) then
                        hormsum[k] = hormsum[k] + exhorm[k]
                    end
                end
            end
        end
    end

    for i=1,#hormsum do
        if count[i] == 0 then
            hormnorm[i] = 0
        else
            hormnorm[i] = hormsum[i]/count[i]
        end
    end
      
    return hormnorm

end
