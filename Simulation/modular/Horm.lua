function ghormone(connh,sensorR,sensorD,baseprob)
    local hormones = {}
    local sensed = false
    local r = math.random()	
    hormones[1] = -1

    for i=1,#connh do
        if (connh[i] == -1) then
            if (sensorR[i] == 1) then
            --print('Generated '..i+1)              
                hormones[i+1] = 1-(sensorD[i]/0.2)
                sensed = true
            else
                hormones[i+1] = -1
            end
        else
            hormones[i+1] = -1
        end
    end

  --  if not sensed then
    --    hormones[1] = 1
   -- end
    if(r>baseprob) then
        hormones[1] = 1
    end

    return hormones,sensed               
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

function receptors(hormones,ampd,offd,phasediff,v,ampset,offsetset,phasediffset,vset,delta)
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

function propagate(prob)
    test = math.random()
    if(test>prob) then --Threshold 0.75
        return true
    else
        return false
    end
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
