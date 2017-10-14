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

function receptorsdelt(hormones,ampd,offd,phasediff,v,ampset,offsetset,phasediffset,vset)
    local ampdnew = ampd
    local offdnew = offd
    local vnew = v
    local phasediffnew ={}      
    for j=1,#phasediff do
        phasediffnew[j] = phasediff[j]
    end

    for k=1,#hormones do
        if (hormones[k]~=-1) then
            ampdnew = ampdnew + (ampset[k]*hormones[k])
            if (ampdnew > 1) then ampdnew = 1 end
            if (ampdnew < -1) then ampdnew = -1 end 

            
            offdnew = offdnew + (offsetset[k]*hormones[k])
            if (offdnew > 1) then offdnew = 1 end
            if (offdnew < -1) then offdnew = -1 end


            for i=1,#phasediffset[k] do
                phasediffnew[i] = phasediffnew[i] + (phasediffset[k][i]*hormones[k])
                if (phasediffnew[i] > math.pi) then phasediffnew[i] = math.pi end
                if (phasediffnew[i] < -math.pi) then phasediffnew[i] = -math.pi end
            end
            
            vnew = vnew + (vset[k]*hormones[k])
            if (vnew >1) then vnew = 1 end
            if (vnew <0) then vnew = 0 end
        end
    end

    return ampdnew,offdnew,phasediffnew,vnew
end

function receptorsf(hormones,ampd,offd,phasediff,v,ampset,offsetset,phasediffset,vset,delta,count)
    local sorted = sortbycount(count)
    local ampdnew = ampd
    local offdnew = offd
    local vnew = v
    local phasediffnew ={}      --Phasediff
    for j=1,#phasediff do
        phasediffnew[j] = phasediff[j]
    end

    for k=1,#hormones do
        --print(hormones[k])
        if (hormones[k]~=-1) then
            --print('Received '..k)
            if(ampdnew<ampset[sorted[k]]) then   --Amplitude
                ampdnew = ampdnew + (delta*hormones[k])
                if (ampdnew > 1) then ampdnew = 1 end
            elseif (ampdnew>ampset[sorted[k]]) then
                ampdnew = ampdnew - (delta*hormones[k])
                if (ampdnew < -1) then ampdnew = -1 end
            end    
            if(offdnew<offsetset[sorted[k]]) then  --Offset
                offdnew = offdnew + (delta*hormones[k])
                if (offdnew > 1) then offdnew = 1 end
            elseif (offdnew>offsetset[sorted[k]]) then
                offdnew = offdnew - (delta*hormones[k])
                if (offdnew < -1) then offdnew = -1 end
            end    
            for i=1,#phasediffset[sorted[k]] do
                if (phasediffnew[i]<phasediffset[sorted[k]][i]) then
                    phasediffnew[i] = phasediffnew[i] + (delta*hormones[k])
                    if (phasediffnew[i] > math.pi) then phasediffnew[i] = math.pi end
                elseif (phasediffnew[i]>phasediffset[sorted[k]][i]) then
                    phasediffnew[i] = phasediffnew[i] - (delta*hormones[k])
                    if (phasediffnew[i] < -math.pi) then phasediffnew[i] = -math.pi end
                end
            end
            if(vnew<vset[sorted[k]]) then   --Frequency
                vnew = vnew + (delta*hormones[k])
                if (vnew >1) then vnew = 1 end
            elseif (vnew>vset[sorted[k]]) then
                vnew = vnew - (delta*hormones[k])
                if (vnew <0) then vnew = 0 end
            end
        end
    end

    return ampdnew,offdnew,phasediffnew,vnew

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