function propagateprob(prob)
    test = math.random()
    if(test>prob) then --Threshold 0.75
        return true
    else
        return false
    end
end

function attenuate(horm)
    --Attenuates all components of a hormone by an attenuation factor
    --factor must be less than 1 for attenuation
    local hormnew={}
    local factor = 0.5--0.2
    local active = false
    for i=1,#horm do
	hormnew[i] = horm[i]
    end
  
    
    for i=1,#hormnew do
        if(hormnew[i]~=-1) then
            hormnew[i]=hormnew[i]*factor
	    if (hormnew[i] > 0.001) then
		active = true
	    end
        end
    end
    return hormnew,active
end

function attenuateprop(rhorm)
    local hormnew ={}
    local rhormnew ={}
    local active = {}
    for i=1,#rhorm do
        rhormnew[i]= {}
        active[i]= {}
    end

    for i=1,#rhorm do
        if (#rhorm[i] > 0) then
            for j=1,#rhorm[i] do    
                local exhorm = simUnpackFloatTable(rhorm[i][j])
                hormnew,active[i][j] = attenuate(exhorm)
                rhormnew[i][j] = simPackFloatTable(hormnew)
            end
        end
    end

    return rhormnew,active
end

function probprop(rhorm,prob)
    local rhormnew ={}
    local active = {}
    for i=1,#rhorm do
        rhormnew[i]= {}
        active[i]= {}
    end

    for i=1,#rhorm do
        if (#rhorm[i] > 0) then
            for j=1,#rhorm[i] do    
                rhormnew[i][j] = rhorm[i][j]
                active[i][j] = propagateprob(prob) 
            end
        end
    end

    return rhormnew,active
end


function forwarddir(rhorm,active,connh)
    local phorm = {}
    for i=1,#rhorm do
        phorm[i]= {}
    end

    for i=1,#connh do
        for j=1,#connh do
            if (j~=i and connh[j]~=-1 and #rhorm[j]>0) then
                for k=1,#rhorm[j] do
                    if(active[j][k]) then
                        table.insert(phorm[i],rhorm[j][k])
                    end
                end
            end
        end
    end

    return phorm
end