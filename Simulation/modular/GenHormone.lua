function ghormonebase(connh,sensorR,sensorD,baseprob)
    local hormones = {}
    local sensed = false
   -- local r = math.random()	
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

    --if not sensed then
        hormones[1] = 1
    --end
    --if(r>baseprob) then
        --hormones[1] = 1
    --end

    return hormones,sensed               
end