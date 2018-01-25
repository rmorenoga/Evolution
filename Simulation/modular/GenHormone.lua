function ghormonebase(connh,sensorR,sensorD,baseprob,ori)
    local hormones = {}
    local sensed = false
    local ground  = 0
   -- local r = math.random()	
    hormones[1] = -1

    ground = groundsen(ori)

    for i=1,#connh do
        if (connh[i] == -1 and ground ~=i) then
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

    hormones[#connh+2] = -1
    hormones[#connh+3] = -1
    --if not sensed then
        hormones[1] = 1
    --end
    --if(r>baseprob) then
        --hormones[1] = 1
    --end

    return hormones,sensed               
end

function ghormoneconn(connh,sensorR,sensorD,ori)
    local hormones = {}
    local sensed = false
    local ground  = 0  

    ground = groundsen(ori)

    for i=1,#connh do
        if (connh[i] == -1 and ground ~=i) then
            if (sensorR[i] == 1) then
            --print('Generated '..i+1)              
                hormones[i] = 1-(sensorD[i]/0.2)
                sensed = true
            else
                hormones[i] = -1
            end
        else
            hormones[i] = -1
        end
    end

    hormones[#connh+1] = -1
    hormones[#connh+2] = -1

    return hormones,sensed

end

function ghormonefront(connh,sensorR,sensorD,ori)

    local hormones = {}
    local sensed = false
    local ground = 0

    ground  = groundsen(ori)

    if (connh[1] == -1 and ground ~= 1) then
        if (sensorR[1] == 1) then
            hormones[1] =  1-(sensorD[1]/0.2)
            sensed = true
        else
            hormones[1]= -1
        end
    else
        hormones[1] = -1

    end

    return hormones,sensed

end